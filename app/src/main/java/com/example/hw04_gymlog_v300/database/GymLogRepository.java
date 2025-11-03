package com.example.hw04_gymlog_v300.database;

import android.app.Application;
import android.util.Log;

import com.example.hw04_gymlog_v300.database.entities.GymLog;
import com.example.hw04_gymlog_v300.MainActivity;
import com.example.hw04_gymlog_v300.database.entities.User;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class GymLogRepository {
    private GymLogDAO gymLogDAO;

    private UserDAO userDAO;
    private ArrayList<GymLog> allLogs;

    private static GymLogRepository repository;

    private GymLogRepository(Application application) {
        GymLogDatabase db = GymLogDatabase.getDataBase(application);
        this.gymLogDAO = db.gymLogDAO();
        this.userDAO = db.userDAO();
    }

    public static GymLogRepository getRepository(Application application) {
        if (repository == null) {
            synchronized (GymLogRepository.class) {
                if (repository == null) {
                    repository = new GymLogRepository((Application) application.getApplicationContext());
                }
            }
        }
        return repository;
    }

    public ArrayList<GymLog> getAllLogs() {
        Future<ArrayList<GymLog>> future = GymLogDatabase.databaseWriteExecutor.submit(
                () -> new ArrayList<>(gymLogDAO.getAllRecords())
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem getting all GymLogs in repository", e);
            return new ArrayList<>(); //
        }
    }

    public void insertGymLog(GymLog gymLog){
        GymLogDatabase.databaseWriteExecutor.execute(() -> {
            gymLogDAO.insert(gymLog);
        });
    }
    public void insertUser(User... user){
        GymLogDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.insert(user);
        });
    }

}
