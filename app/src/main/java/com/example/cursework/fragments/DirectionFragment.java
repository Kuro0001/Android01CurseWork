package com.example.cursework.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.cursework.Authorisation;
import com.example.cursework.R;
import com.example.cursework.Validation;
import com.example.cursework.dataBase.DBDirections;
import com.example.cursework.dataBase.DBHelper;
import com.example.cursework.databinding.FragmentDirectionBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DirectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DirectionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Toast toast;
    public static final String tagDB = "tagFragment";
    private FragmentDirectionBinding binding;
    DBHelper dbHelper;
    SQLiteDatabase database;
    SimpleCursorAdapter simpleCursorAdapter;
    Cursor cursor;
    int directionId;

    public static String[] from = new String[] {
            DBHelper.KEY_DIRECTIONS_NAME
    };
    public static int[] to = new int[]{
            R.id.etDirectionName
    };

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DirectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DirectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DirectionFragment newInstance(String param1, String param2) {
        DirectionFragment fragment = new DirectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(tagDB, "?????????? ???????????? onCreate ?????????????????? DirectionFragment");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(tagDB, "?????????? ???????????? onCreateView ?????????????????? DirectionFragment");
        // Inflate the layout for this fragment
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        binding = FragmentDirectionBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        binding.btnAdd.setOnClickListener(this::onAdd);
        binding.btnEdit.setOnClickListener(this::onEdit);
        binding.btnDelete.setOnClickListener(this::onDelete);
        binding.lvDirections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                directionId = (int)l;
                if (rowIsExist(DBHelper.TABLE_NAME_DIRECTIONS,DBHelper.KEY_DIRECTIONS_ID,directionId)) {
                    Cursor item = (Cursor) simpleCursorAdapter.getItem(i);
                    binding.etEnterName.setText(item.getString(item.getColumnIndexOrThrow(from[0])));
                }
            }
        });

        if (Authorisation.isLoggedIn) setEnabled(true);
        else  setEnabled(false);
        dbHelper = new DBHelper(getContext());
        readDB();
        return root;
    }

    @Override
    public void onDestroyView() {
        Log.d(tagDB, "?????????? ???????????? onDestroyView ?????????????????? DirectionFragment");
        super.onDestroyView();
        binding = null;
//        cursor.close();
    }

    /**
     * ?????????? ???????????????????????????? ?? ???????????????? ?????????????? ?? ?????????????????????? ?????????????????? ????
     * @param status
     */
    public void setEnabled(boolean status){
            binding.btnAdd.setEnabled(status);
            binding.btnEdit.setEnabled(status);
            binding.btnDelete.setEnabled(status);
        if (directionId < 0){
            binding.btnEdit.setEnabled(false);
            binding.btnDelete.setEnabled(false);
        }
    }

    /**
     * ?????????? ???????????????????? ???????????? ???? ????
     */
    public void readDB(){
        Log.d(tagDB, "?????????? ???????????? readDB ?????????????????? DirectionFragment");
        database = dbHelper.getReadableDatabase();
        cursor = database.query(DBHelper.TABLE_NAME_DIRECTIONS,
                null,
                null,
                null,
                null,
                null,
                null);
        simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_direction , cursor, from, to, 0);
        binding.lvDirections.setAdapter(simpleCursorAdapter);

        binding.lvDirections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                directionId = (int)l;
                if (rowIsExist(DBHelper.TABLE_NAME_DIRECTIONS,DBHelper.KEY_DIRECTIONS_ID,directionId)) {
                    Cursor item = (Cursor) simpleCursorAdapter.getItem(i);
                    binding.etEnterName.setText(item.getString(item.getColumnIndexOrThrow(from[0])));
                    setEnabled(true);
                }else {
                    setEnabled(false);
                }
            }
        });

        if(cursor.moveToFirst() == true){
            //?????????????????? ????????????????
            do {
                simpleCursorAdapter.changeCursor(cursor);
//                binding.lvDirections.setOnItemClickListener(this::onClickList);
                binding.lvDirections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        directionId = (int)l;
                        if (rowIsExist(DBHelper.TABLE_NAME_DIRECTIONS,DBHelper.KEY_DIRECTIONS_ID,directionId)) {
//            binding.edEnterName.setText(String.valueOf(directionId));
                            Cursor item = (Cursor) simpleCursorAdapter.getItem(i);
                            binding.etEnterName.setText(item.getString(item.getColumnIndexOrThrow(from[0])));
                            setEnabled(true);
                        }else {
                            setEnabled(false);
                        }
                    }
                });
            } while (cursor.moveToNext() == true);
        }
//        cursor.close();
    }

    /**
     * ?????????? ?????????????????????????? ???????????????????????? ???? ?????????????? ???????????? (?????????????? ?????????????? ???? ?????????????? ????????????)
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    public void onClickList(AdapterView<?> parent, View view, int position, long id){
        directionId = (int)id;
        if (rowIsExist(DBHelper.TABLE_NAME_DIRECTIONS,DBHelper.KEY_DIRECTIONS_ID,directionId)) {
            Cursor item = (Cursor) simpleCursorAdapter.getItem(position);
            binding.etEnterName.setText(item.getString(item.getColumnIndexOrThrow(from[0])));
        }else {
        }
    }

    /**
     * ???????????????? ?????????????? ?????????????????? ???????????? ???????????? ?? ????
     * @param table
     * @param columnName
     * @param id
     * @return
     */
    public boolean rowIsExist(String table, String columnName, int id){
        database = dbHelper.getReadableDatabase();
        cursor = database.query(table,
                null,
                columnName + " LIKE ?",
                new String[] {
                        id + "%"},
                null,
                null,
                null);
        if(cursor.moveToFirst() == true)
            return true;
        return false;
    }

    /**
     * ?????????? ?????? ???????????????? ???????????????? ???????????? ??????????????????????????
     * @return
     */
    public boolean isCorrectInput(){
        String s = binding.etEnterName.getText().toString();
        if(!Validation.isRightName(binding.etEnterName.getText().toString())) return false;
        return true;
    }

    /**
     * ?????????? ???????????????????? ???????????? ?? ????
     * @param view
     */
    public void onAdd(View view){
        Log.d(tagDB, "?????????? ???????????? onAdd ?????????????????? DirectionFragment");
        database = dbHelper.getWritableDatabase();
        if(!isCorrectInput()) {
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct), Toast.LENGTH_LONG);
            Log.d(tagDB, getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct));
        }
        else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_DIRECTIONS_NAME, binding.etEnterName.getText().toString());

            long result = database.insert(DBHelper.TABLE_NAME_DIRECTIONS, null, contentValues);

            if (result > 0) {
                Log.d(tagDB, getResources().getString(R.string.action_result_OK));
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_add_result_OK), Toast.LENGTH_LONG);
                readDB();
            } else {
                Log.d(tagDB, getResources().getString(R.string.action_result_ERROR));
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_ERROR), Toast.LENGTH_LONG);
            }
        }
        toast.show();
    }

    /**
     * ?????????? ?????????????????? ?????????????????? ???????????? ???????????? ?? ????
     * @param view
     */
    public void onEdit(View view){
        Log.d(tagDB, "?????????? ???????????? onEdit ?????????????????? DirectionFragment");
        if(!isCorrectInput()) {
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct), Toast.LENGTH_LONG);
            Log.d(tagDB, getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct));
        }
        else {
            database = dbHelper.getWritableDatabase();
            if (rowIsExist(DBHelper.TABLE_NAME_DIRECTIONS, DBHelper.KEY_DIRECTIONS_ID, directionId)) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_DIRECTIONS_NAME, binding.etEnterName.getText().toString());
                String where = DBHelper.KEY_DIRECTIONS_ID + "=" + directionId;

                long result = database.update(DBHelper.TABLE_NAME_DIRECTIONS, contentValues, where, null);
                if (result > 0) {
                    Log.d(tagDB, getResources().getString(R.string.action_result_OK));
                    toast = Toast.makeText(getContext(), getResources().getString(R.string.action_edit_result_OK), Toast.LENGTH_LONG);
                    readDB();
                } else {
                    Log.d(tagDB, getResources().getString(R.string.action_result_ERROR));
                    toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_ERROR), Toast.LENGTH_LONG);
                }
            } else {
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_NOT_OK)
                        + " - " + getResources().getString(R.string.db_row_cant_find), Toast.LENGTH_LONG);
                Log.d(tagDB, getResources().getString(R.string.action_result_NOT_OK)
                        + " - " + getResources().getString(R.string.db_row_cant_find));
            }
        }
        toast.show();
    }

    /**
     * ?????????? ???????????????? ?????????????????? ???????????? ???? ????
     * @param view
     */
    public void onDelete(View view){
        Log.d(tagDB, "?????????? ???????????? onDelete ?????????????????? DirectionFragment");
        database = dbHelper.getWritableDatabase();

        if (rowIsExist(DBHelper.TABLE_NAME_DIRECTIONS,DBHelper.KEY_DIRECTIONS_ID,directionId)) {
            if (!rowIsExist(DBHelper.TABLE_NAME_HOTELS, DBHelper.KEY_HOTELS_ID_DIRECTION, directionId)) {
                long result = database.delete(dbHelper.TABLE_NAME_DIRECTIONS,
                        DBHelper.KEY_DIRECTIONS_ID + " = ?", new String[]{String.valueOf(directionId)});
                if (result > 0) {
                    Log.d(tagDB, getResources().getString(R.string.action_result_OK));
                    toast = Toast.makeText(getContext(), getResources().getString(R.string.action_delete_result_OK), Toast.LENGTH_LONG);
                    readDB();
                } else {
                    Log.d(tagDB, getResources().getString(R.string.action_result_ERROR));
                    toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_ERROR), Toast.LENGTH_LONG);
                }
            } else {
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_NOT_OK)
                        + " - " + getResources().getString(R.string.db_row_in_use), Toast.LENGTH_LONG);
                Log.d(tagDB, getResources().getString(R.string.action_result_NOT_OK)
                        + " - " + getResources().getString(R.string.db_row_in_use));
            }
        }else {
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.db_row_cant_find),Toast.LENGTH_LONG);
            Log.d(tagDB, getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.db_row_cant_find));

        }
        toast.show();
    }
}