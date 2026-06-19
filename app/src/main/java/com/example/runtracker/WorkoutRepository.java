package com.example.runtracker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Stores and reads the user's custom workouts under Firestore. */
public class WorkoutRepository {

    public interface OnWorkoutsLoaded {
        void onLoaded(List<Workout> workouts);
    }

    public interface OnComplete {
        void onComplete(boolean success);
    }

    public void save(String name, ActivityType type, String goalType, double goalValue, OnComplete callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            callback.onComplete(false);
            return;
        }

        Map<String, Object> workout = new HashMap<>();
        workout.put("name", name);
        workout.put("type", type.key);
        workout.put("goalType", goalType);
        workout.put("goalValue", goalValue);
        workout.put("timestamp", FieldValue.serverTimestamp());

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getUid())
                .collection("workouts")
                .add(workout)
                .addOnSuccessListener(ref -> callback.onComplete(true))
                .addOnFailureListener(e -> callback.onComplete(false));
    }

    public void loadWorkouts(OnWorkoutsLoaded callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            callback.onLoaded(Collections.emptyList());
            return;
        }

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getUid())
                .collection("workouts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Workout> workouts = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        workouts.add(Workout.fromDocument(doc));
                    }
                    callback.onLoaded(workouts);
                })
                .addOnFailureListener(e -> callback.onLoaded(Collections.emptyList()));
    }
}
