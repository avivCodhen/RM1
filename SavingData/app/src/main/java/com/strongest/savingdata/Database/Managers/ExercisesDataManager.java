package com.strongest.savingdata.Database.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.strongest.savingdata.AlgorithmGenerator.BParams;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.DEFAULT_INT;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.DETAIL;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.D_A_LEGS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.D_A_SHOULDERS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.D_BACK;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.D_BICEPS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.D_CALVES;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.D_CHEST;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.D_CORE;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.D_LOWER_BACK;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.D_P_LEGS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.D_R_SHOULDERS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.D_TRAPEZIUS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.D_TRICEPS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.D_WRIST;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.ID;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.IMAGE;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.INTENSITY;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.LEVEL;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.MECHANICAL_STRESS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.METABOLIC_STRESS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.MUSCLE;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.MUSCLES;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.M_A_SHOULDERS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.M_BICEPS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.M_R_SHOULDERS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.M_TRICEPS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.NAME;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.PYRAMID;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.P_ANTERIOR;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.P_POSTERIOR;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.SETS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_EXERCISES_CUSTOM;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_EXERCISES_GENERATOR;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_METHODS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REPS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REPS_GENERATOR;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REST;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_SETS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TYPE;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.WEIGHT;

/**
 * Created by Cohen on 6/13/2017.
 */

public class ExercisesDataManager extends DataManager implements DataManagerListener<Beans> {

    private SQLiteDatabase db;
    private DataManager parent;
    private Context context;
    private DBExercisesHelper helper;

    public ExercisesDataManager(DataManager parent, Context context) {
        super(context);
        this.parent = parent;

        this.context = context;
        this.helper = new DBExercisesHelper(context);
        db = helper.getWritableDatabase();

    }

    /*public void updateBeans(String tableName, Beans muscle) {
        Cursor c = db.rawQuery("SELECT * FROM " + tableName, null);

        db.rawQuery("UPDATE " + tableName + " SET _id=? _id+1 WHERE _id >= 0", null);
        db.insert(tableName,
                null,
                getBeansContentValues(tableName, muscle)
        );
        db.rawQuery("UPDATE " + tableName + " SET _id=0 " + " WHERE _id=?", new String[]{String.valueOf(c.getCount())});

    }*/


    public boolean isConnectionAvailable() {
        boolean exists = false;
        final String BASE_URL = "http://10.0.0.58:8080/";
        try {
            SocketAddress sockaddr = new InetSocketAddress("http://10.0.0.58", 8080);
            // Create an unbound socket
            Socket sock = new Socket();

            // This method will block no more than timeoutMs.
            // If the timeout occurs, SocketTimeoutException is thrown.
            int timeoutMs = 2000;   // 2 seconds
            sock.connect(sockaddr, timeoutMs);
            exists = true;
            sock.close();
        } catch (IOException e) {
            // Handle exception
        }


        return exists;
    }

    public void delete(String table) {
        db.delete(table, null, null);
        db.delete("SQLITE_SEQUENCE", "NAME = ?", new String[]{table});
    }

    public ArrayList<String> readListByTable(String table){
        ArrayList<String> arr = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ table, null);
        if(cursor != null && cursor.moveToFirst()){
            do{
              arr.add(cursor.getString(cursor.getColumnIndex(NAME)));
            }while ((cursor.moveToNext()));
        }
        return arr;
    }



    public Collection<Beans> readByBParams(String tableName, BParams... p) {
        String[] s = new String[p.length];
        String[] c = new String[p.length];
        for (int i = 0; i < p.length; i++) {
            s[i] = p[i].getName();
            String v = String.valueOf(p[i].getValue());
            c[i] = v;
        }
        return readByConstraint(tableName, s, c);
    }

    public Beans fetchByName(String table, String name) {
        ArrayList<Beans> arr = (ArrayList<Beans>) readByConstraint(table, new String[]{NAME},
                new String[]{name});
        if (arr.size() == 0) {
            return null;
        }
        return arr.get(0);
    }

    public void removeByName(String table, String name){
        String nameWhere = "name=?";
        db.delete(table, nameWhere, new String[]{name});
    }

    public void removeById(String table, int id) {
        String idWhere = "id=?";
        db.delete(table, idWhere, new String[]{String.valueOf(id)});
    }
    @Deprecated
    public Beans fetchById(String table, int id) {
        ArrayList<Beans> arr = (ArrayList<Beans>) readByConstraint(table, new String[]{ID},
                new String[]{String.valueOf(id)});
        if (arr.size() == 0) {
            return null;
        }
        return arr.get(0);
    }

    public Collection<Beans> readByTable(String table) {
        return readByConstraint(table, null, null);
    }

    public Collection<Beans> readByString(String tableName, String s, int c) {
        return readByConstraint(tableName, new String[]{s}, new String[]{String.valueOf(c)});
    }



    public void refreshGeneratorTable() {
        delete(TABLE_EXERCISES_GENERATOR);
        ArrayList<Beans> arr = new ArrayList<>();
        for (String t : helper.muscleTables) {
            arr = (ArrayList<Beans>) readByTable(t);
            for (Beans b : arr) {
                insertData(TABLE_EXERCISES_GENERATOR, b);
            }
        }
    }

    public Collection<Beans> readByConstraint(String muscleName, String[] constraints, String[] values) {
        String condition;
        String tableName = "";
        switch (muscleName) {

            case "anterior_shoulders":
                tableName = "shoulders";
                break;
            case "middle_shoulders":
                tableName = "shoulders";

                break;
            case "posterior_shoulders":
                tableName = "shoulders";

                break;
            case "triceps":
                tableName = "arms";

                break;
            case "biceps":
                tableName = "arms";

                break;
            case "quadriceps":
                tableName = "legs";

                break;
            case "hip":
                tableName = "legs";

                break;
            case "hamstring":
                tableName = "legs";

                break;
            case "calf":
                tableName = "legs";
                break;
            default:
                tableName = muscleName;
        }
    /*    if (values != null && values[0].equals("4") && constraints[0].equals(MUSCLE)) {


            values[1] = "1";
            for (int i = 0; i < constraints.length; i++) {
                if (constraints[i].equals(M_TRICEPS)) {
                    values[0] = "5";

                } else {
                    values[0] = "6";
                }
            }
            //condition = " OR ";

        }*/

        condition = " AND ";

        String sql = "SELECT * FROM " + tableName;
        String w = " = ? ";
        String where = " WHERE ";
        String like = " LIKE ";

        if (constraints != null) {
            sql += where;
            for (int i = 0; i < constraints.length; i++) {
                if (constraints[i].equals(MUSCLES)) {
                    sql += constraints[i]+ like + "?";
                    values[i] = "%"+values[i]+"%";
                } else {
                    sql += constraints[i] + w;

                }

                if (constraints.length > 1 && i != constraints.length - 1) {
                    sql += condition;
                }

            }

        }
        sql += " ORDER BY id ASC";
        Cursor c = db.rawQuery(sql, values);
        if (values != null) {

        }
        return parse(tableName, c);
    }

    @Override
    public Collection<Beans> parse(String tableName, Cursor c) {
        return parseBean(tableName, c);
    }


    public void insertData(String tableName, Beans b) {

        db.insert(tableName,
                null,
                getBeansContentValues(tableName, b)
        );
    }

    public void insertData(String tableName, String s) {
        ContentValues c = new ContentValues();
        c.put(NAME, s);

        db.insert(tableName,
                null,
                c
        );
    }


    public void query(String[] colums) {

    }


    /*public void saveObjectToFile(Object obj, SharedPreferences prefs){
        int counter = prefs.getInt(PROGRAM_VALUE, 0);
        counter++;
            saveObjectToFile(obj, PROGRAM_FILE+counter);
        }
    }*/


    @Override
    public void close() {
        if (db != null) {
            db.close();
        }
    }


    public ContentValues getBeansContentValues(String table, Beans e) {
        ContentValues v = new ContentValues();
        table = table.toLowerCase();

        if (table.equals(TABLE_REST)) {
            v.put(TYPE, e.getType());
            v.put(ID, e.getId());
            v.put(NAME, e.getName());
            v.put(DEFAULT_INT, e.getDefault_int());
            return v;

        }
        if (table.equals(TABLE_SETS)) {
            v.put(TYPE, e.getType());
            v.put(ID, e.getId());
            v.put(NAME, e.getName());
            v.put(DEFAULT_INT, e.getDefault_int());
            return v;
        }
        if (table.equals(TABLE_METHODS)) {
            v.put(ID, e.getId());
            v.put(NAME, e.getName());
            v.put(DEFAULT_INT, e.getDefault_int());
            v.put(LEVEL, e.getLevel());
            v.put(DETAIL, e.getDetails());
            return v;
        }


        if (table.equals(TABLE_REPS)) {
            v.put(ID, e.getId());
            v.put(NAME, e.getName());
            v.put(DEFAULT_INT, e.getDefault_int());
            v.put(LEVEL, e.getLevel());
            v.put(DETAIL, e.getDetails());
            v.put(PYRAMID, e.getPyramid());
            v.put(INTENSITY, e.getIntensity());
            v.put(MECHANICAL_STRESS, e.getMechanicalStress());
            v.put(METABOLIC_STRESS, e.getMetabolicStress());
            v.put(SETS, e.getSets());

            return v;
        }
        boolean flag = false;
        boolean custom = table.equals(TABLE_EXERCISES_CUSTOM);
        if(custom){
            v.put(NAME, e.getName());
            v.put(MUSCLE, e.getPrimaryMuscle());
            v.put(DEFAULT_INT, e.getDefault_int());
            return v;

        }
        for (String t : helper.muscleTables) {
            if (t.equals(table) || table.equals(TABLE_EXERCISES_GENERATOR)) {
                flag = true;
            }
        }

        if (flag) {
            v.put(LEVEL, e.getLevel());
            v.put(DETAIL, e.getDetails());
            v.put(TYPE, e.getType());
            v.put(ID, e.getId());
            v.put(NAME, e.getName());
            v.put(WEIGHT, e.getWeight());
            v.put(TYPE, e.getType());
            v.put(IMAGE, e.getImage());
            v.put(MUSCLE, e.getPrimaryMuscle());
            v.put(MUSCLES, e.getMuscles());


            v.put(D_WRIST, e.getWrist());
            v.put(D_CHEST, e.getChest());
            v.put(D_BACK, e.getBack());
            v.put(D_BICEPS, e.getBiceps());
            v.put(D_TRICEPS, e.getTriceps());
            v.put(D_CORE, e.getCore());
            v.put(D_CALVES, e.getCalves());
            v.put(D_TRAPEZIUS, e.getWrist());
            v.put(D_LOWER_BACK, e.getLower_back());
            v.put(D_R_SHOULDERS, e.getR_shoulders());
            v.put(D_A_SHOULDERS, e.getA_shoulders());
            v.put(D_P_LEGS, e.getPosteriorLegs());
            v.put(D_A_LEGS, e.getAnteriorLegs());

            v.put(P_POSTERIOR, e.getP_legs_posterior());
            v.put(P_ANTERIOR, e.getP_legs_anterior());
            v.put(M_A_SHOULDERS, e.getM_anterior_shoulders());
            v.put(M_R_SHOULDERS, e.getM_rear_shoulders());
            v.put(M_TRICEPS, e.getM_triceps());
            v.put(M_BICEPS, e.getM_biceps());

            return v;

        }
       /* if (table.equals(TABLE_EXERCISES_ALL) || table.equals(TABLE_EXERCISES_GENERATOR)) {
            v.put(LEVEL, e.getLevel());
            v.put(DETAIL, e.getDetails());
            v.put(TYPE, e.getType());
            v.put(ID, e.getBean());
            v.put(NAME, e.getName());
            v.put(WEIGHT, e.getWeight());
            v.put(TYPE, e.getType());
            v.put(IMAGE, e.getImage());
            v.put(MUSCLE, e.getMuscle().getMuscle_name());


            v.put(D_WRIST, e.getWrist());
            v.put(D_CHEST, e.getChest());
            v.put(D_BACK, e.getBack());
            v.put(D_BICEPS, e.getBiceps());
            v.put(D_TRICEPS, e.getTriceps());
            v.put(D_CORE, e.getCore());
            v.put(D_CALVES, e.getCalves());
            v.put(D_TRAPEZIUS, e.getWrist());
            v.put(D_LOWER_BACK, e.getLower_back());
            v.put(D_R_SHOULDERS, e.getR_shoulders());
            v.put(D_A_SHOULDERS, e.getA_shoulders());
            v.put(D_P_LEGS, e.getPosteriorLegs());
            v.put(D_A_LEGS, e.getAnteriorLegs());

            v.put(P_POSTERIOR, e.getP_legs_posterior());
            v.put(P_ANTERIOR, e.getP_legs_anterior());
            v.put(M_A_SHOULDERS, e.getM_anterior_shoulders());
            v.put(M_R_SHOULDERS, e.getM_rear_shoulders());
            v.put(M_TRICEPS, e.getM_triceps());
            v.put(M_BICEPS, e.getM_biceps());

            return v;

        }*/

        return null;

    }

    public Collection<Beans> parseBean(String table, Cursor c) {
        List<Beans> e = new ArrayList();
        //  int[] exerciseIcons;
        //  int position = 0;
        table = table.toLowerCase();
        if (c != null && c.moveToFirst()) {
            do {
                Beans ex = new Beans();
                if (table.equals(TABLE_SETS)) {
                    String t = c.getString(c.getColumnIndex(TYPE));
                    String name = c.getString(c.getColumnIndex(NAME));
                    int id = c.getInt((c.getColumnIndex(ID)));
                    int defaultInt = c.getInt(c.getColumnIndex(DEFAULT_INT));

                    ex = Beans.createSets(id, name, t, defaultInt);

                   /* ex.setDefault_int(defaultInt);
                    ex.setBean(id);
                    ex.setType(t);
                    ex.setName(name);*/
                }
                if (table.equals(TABLE_REST)) {
                    String t = c.getString(c.getColumnIndex(TYPE));
                    String name = c.getString(c.getColumnIndex(NAME));
                    int id = c.getInt((c.getColumnIndex(ID)));
                    int defaultInt = c.getInt(c.getColumnIndex(DEFAULT_INT));
                    ex = Beans.createSets(id, name, t, defaultInt);

                    /*ex.setDefault_int(defaultInt);
                    ex.setBean(id);
                    ex.setType(t);
                    ex.setName(name);*/
                }
                /*if (table.equals(TABLE_METHODS)) {
                    int level = c.getInt(c.getColumnIndex(LEVEL));
                    String details = c.getString(c.getColumnIndex(DETAIL));
                    String name = c.getString(c.getColumnIndex(NAME));
                    int id = c.getInt((c.getColumnIndex(ID)));
                    int defaultInt = c.getInt(c.getColumnIndex(DEFAULT_INT));
                    ex.setLevel(level);
                    ex.setDetails(details);
                    ex.setDefault_int(defaultInt);
                    ex.setBean(id);
                    ex.setName(name);
                }*/

                if (table.equals(TABLE_REPS) || table.equals(TABLE_REPS_GENERATOR)) {
                    int level = c.getInt(c.getColumnIndex(LEVEL));
                    String detail = c.getString(c.getColumnIndex(DETAIL));
                    int mec = c.getInt((c.getColumnIndex(MECHANICAL_STRESS)));
                    int met = c.getInt((c.getColumnIndex(METABOLIC_STRESS)));
                    String name = c.getString(c.getColumnIndex(NAME));
                    int id = c.getInt((c.getColumnIndex(ID)));
                    int defaultInt = c.getInt(c.getColumnIndex(DEFAULT_INT));
                    int pyr = c.getInt(c.getColumnIndex(PYRAMID));
                    int intensity = c.getInt(c.getColumnIndex(INTENSITY));
                    ex = Beans.createReps(defaultInt, id, name, intensity, detail, pyr, mec, met);

                }
                boolean flag = false;
                boolean custom = table.equals(TABLE_EXERCISES_CUSTOM);
                for (String t : helper.muscleTables) {
                    if (t.equals(table) || table.equals(TABLE_EXERCISES_GENERATOR)){
                        flag = true;
                    }
                }
                if(custom){
                    String name = c.getString(c.getColumnIndex(NAME));
                    String primaryMuscle = c.getString((c.getColumnIndex(MUSCLE)));
                    Muscle m = Muscle.createMuscle(parent.getMuscleDataManager(), primaryMuscle);
                    int default_int = c.getInt(c.getColumnIndex(DEFAULT_INT));
                    ex = new Beans();
                    ex.name = name;
                    ex.primaryMuscle = primaryMuscle;
                    ex.muscle = m;
                    ex.setDefault_int(default_int);
                }

                if (flag) {

                    int level = c.getInt(c.getColumnIndex(LEVEL));
                    String detail = c.getString(c.getColumnIndex(DETAIL));
//                    int mec = c.getInt((c.getColumnIndex(MECHANICAL_STRESS)));
                    //                  int met = c.getInt((c.getColumnIndex(METABOLIC_STRESS)));
                    String t = c.getString(c.getColumnIndex(TYPE));
                    String name = c.getString(c.getColumnIndex(NAME));
                    int id = c.getInt((c.getColumnIndex(ID)));
                    double weight = c.getDouble(c.getColumnIndex(WEIGHT));
                    String image = c.getString((c.getColumnIndex(IMAGE)));
                    String muscle = c.getString((c.getColumnIndex(MUSCLES)));
                    String muscles = c.getString((c.getColumnIndex(MUSCLES)));
                    String primaryMuscle = c.getString((c.getColumnIndex(MUSCLE)));
                    muscle = Beans.parsePrimaryMuscle(muscle);
                    Muscle m = Muscle.createMuscle(parent.getMuscleDataManager(), muscle);

                    int m_biceps = c.getInt((c.getColumnIndex(M_BICEPS)));
                    int m_triceps = c.getInt((c.getColumnIndex(M_TRICEPS)));
                    int m_a_s = c.getInt((c.getColumnIndex(M_A_SHOULDERS)));
                    int m_r_s = c.getInt((c.getColumnIndex(M_R_SHOULDERS)));
                    int p_p_l = c.getInt((c.getColumnIndex(P_POSTERIOR)));
                    int p_a_l = c.getInt((c.getColumnIndex(P_ANTERIOR)));
                    int anteriorLegs = c.getInt(c.getColumnIndex(D_A_LEGS));
                    int posteriorLegs = c.getInt(c.getColumnIndex(D_P_LEGS));

                    ex = Beans.createExercise(muscles, primaryMuscle, id, name, t, level, detail, m, image, weight, m_a_s, m_r_s,
                            m_biceps, m_triceps, posteriorLegs, anteriorLegs, p_p_l, p_a_l);

                    /*int[] damages = {
                            c.getInt((c.getColumnIndex(D_CHEST))),
                            c.getInt((c.getColumnIndex(D_BACK))),
                            c.getInt((c.getColumnIndex(D_A_LEGS))),
                            c.getInt((c.getColumnIndex(D_P_LEGS))),
                            c.getInt((c.getColumnIndex(D_A_SHOULDERS))),
                            c.getInt((c.getColumnIndex(D_R_SHOULDERS))),
                            c.getInt((c.getColumnIndex(D_BICEPS))),
                            c.getInt((c.getColumnIndex(D_TRICEPS))),
                            c.getInt((c.getColumnIndex(D_TRAPEZIUS))),
                            c.getInt((c.getColumnIndex(D_LOWER_BACK))),
                            c.getInt((c.getColumnIndex(D_TRAPEZIUS))),
                            c.getInt((c.getColumnIndex(D_CALVES))),
                            c.getInt((c.getColumnIndex(D_WRIST))),

                    };*/
                    /*ex.setLevel(level);
                    ex.setDetails(details);
                    ex.setBean(id);
                    ex.setType(t);
                    ex.setName(name);
                    ex.setWeight(weight);
                    ex.setType(t);
                    ex.setImage(image);
                    ex.setPrimaryMuscle(muscle);

                    ex.setM_anterior_shoulders(m_a_s);
                    ex.setM_rear_shoulders(m_r_s);
                    ex.setM_biceps(m_biceps);
                    ex.setM_triceps(m_triceps);
                    ex.setPosteriorLegs(posteriorLegs);
                    ex.setAnteriorLegs(anteriorLegs);
                    ex.setP_legs_anterior(p_p_l);
                    ex.setP_legs_posterior(p_a_l);*/
                    //ex.setDamages(damages);

                  /*  if (exerciseIcons != null && exerciseIcons.length > id)
                        ex.setImage(exerciseIcons[id]);
*/
                }
                ex.setLoaded(true);

                e.add(ex);
            } while (c.moveToNext());
        }
        return e;
    }
}
