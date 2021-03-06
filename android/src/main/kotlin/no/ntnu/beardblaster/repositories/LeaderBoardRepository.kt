package no.ntnu.beardblaster.repositories

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import no.ntnu.beardblaster.commons.State
import no.ntnu.beardblaster.commons.game.Game
import no.ntnu.beardblaster.commons.leaderboard.AbstractLeaderBoardRepository
import no.ntnu.beardblaster.commons.leaderboard.BeardScore
import no.ntnu.beardblaster.commons.wizard.Wizard

class LeaderBoardRepository(private val db: FirebaseFirestore = Firebase.firestore) :
    AbstractLeaderBoardRepository<Game> {
    private val TAG = "LeaderBoardRepository"
    private val BEARDS_COLLECTION = "beards"

    override fun getBeardLengthForUser(userId: String): Flow<State<Float>> = flow {
        emit(State.loading<Float>())

        val snapshot = db
            .collection(BEARDS_COLLECTION)
            .document(userId)
            .get()
            .await()

        if (!snapshot.exists()) {
            Log.d(
                TAG,
                "No beard found for user: $userId"
            )
            emit(State.Success(0f))
        } else {
            val data = snapshot.toObject<BeardScore>()!!.beardLength
            Log.d(
                TAG,
                "Beard found for user (!): $userId"
            )
            emit(State.Success(data))
        }

    }

    override fun getTopTenBeards(): Flow<State<List<BeardScore>>> = flow {
        emit(State.loading<List<BeardScore>>())

        val snapshot = db
            .collection(BEARDS_COLLECTION)
            .orderBy("beardLength", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .await()

        if (!snapshot.isEmpty) {
            val leaderBoardList: MutableList<BeardScore> = mutableListOf()
            for (doc: DocumentSnapshot in snapshot.documents) {
                val score = doc.toObject<BeardScore>()
                if (score != null) {
                    leaderBoardList.add(score)
                }
            }
            emit(State.success(leaderBoardList.toList()))
        } else {
            emit(State.failed<List<BeardScore>>("Could not fetch leaderboard"))
        }
    }

    override fun updateBeardLength(wizard: Wizard, beardLengthIncrease: Float): Flow<State<BeardScore>> = flow {
            emit(State.loading<BeardScore>())

            val documentReference = db.collection(BEARDS_COLLECTION)
            .document(wizard.id).get().await()

            if(documentReference.exists()) {
                val lastBeardScore = documentReference.toObject<BeardScore>()
                lastBeardScore!!.beardLength += beardLengthIncrease
                db.collection(BEARDS_COLLECTION)
                    .document(wizard.id)
                    .update("beardLength", lastBeardScore!!.beardLength.coerceAtLeast(0f))
                    .await()
                emit(State.success(documentReference.toObject<BeardScore>()!!))

            } else {
                val beardScore = BeardScore(
                    beardLength = beardLengthIncrease.coerceAtLeast(0f),
                    displayName = wizard.displayName,
                    id = wizard.id
                )
                db.collection(BEARDS_COLLECTION)
                    .document(wizard.id)
                    .set(
                        beardScore
                    ).await()
                emit(State.success(beardScore))
            }
        Log.d(
            TAG,
            "Posted new beardlength $beardLengthIncrease cm of user ${wizard.displayName} successfully"
        )
        }
}
