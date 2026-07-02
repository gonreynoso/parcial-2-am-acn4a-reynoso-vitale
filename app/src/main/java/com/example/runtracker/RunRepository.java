package com.example.runtracker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunRepository {

    public interface OnRunsLoaded {
        void onLoaded(List<Run> runs);
    }

    public interface OnComplete {
        void onComplete();
    }

    
    public void deleteAll(OnComplete callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            callback.onComplete();
            return;
        }

        CollectionReference runs = FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getUid())
                .collection("runs");

        runs.get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        callback.onComplete();
                        return;
                    }
                    WriteBatch batch = FirebaseFirestore.getInstance().batch();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        batch.delete(doc.getReference());
                    }
                    batch.commit().addOnCompleteListener(task -> callback.onComplete());
                })
                .addOnFailureListener(e -> callback.onComplete());
    }

    
    public void loadRuns(int limit, OnRunsLoaded callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            callback.onLoaded(Collections.emptyList());
            return;
        }

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getUid())
                .collection("runs")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Run> runs = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        runs.add(Run.fromDocument(doc));
                    }
                    callback.onLoaded(runs);
                })
                .addOnFailureListener(e -> callback.onLoaded(Collections.emptyList()));
    }

    
    public void save(String activityType, double distanceKm, int steps, long durationSeconds) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        if (steps <= 0 && distanceKm <= 0) return;

        Map<String, Object> run = new HashMap<>();
        run.put("tipo", activityType);
        run.put("distanciaKm", distanceKm);
        run.put("pasos", steps);
        run.put("duracionSegundos", durationSeconds);
        run.put("timestamp", FieldValue.serverTimestamp());

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getUid())
                .collection("runs")
                .add(run);
    }
}
