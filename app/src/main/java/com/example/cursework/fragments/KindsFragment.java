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
import com.example.cursework.dataBase.DBHelper;
import com.example.cursework.databinding.FragmentKindsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KindsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KindsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Toast toast;
    public static final String tagDB = "tagFragment";
    private FragmentKindsBinding binding;
    DBHelper dbHelper;
    SQLiteDatabase database;
    SimpleCursorAdapter simpleCursorAdapter;
    Cursor cursor;
    int kindId;

    public static String[] from = new String[] {
            DBHelper.KEY_KINDS_NAME
    };
    public static int[] to = new int[]{
            R.id.etName
    };

    public KindsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KindsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KindsFragment newInstance(String param1, String param2) {
        KindsFragment fragment = new KindsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        binding = FragmentKindsBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        binding.btnAdd.setOnClickListener(this::onAdd);
        binding.btnEdit.setOnClickListener(this::onEdit);
        binding.btnDelete.setOnClickListener(this::onDelete);
        binding.lvKinds.setOnItemClickListener(this::onClickList);
        if (Authorisation.isLoggedIn) setEnabled(true);
        else  setEnabled(false);
        dbHelper = new DBHelper(getContext());
        readDB();
        return root;
        //return inflater.inflate(R.layout.fragment_kinds, container, false);
    }
    @Override
    public void onDestroyView() {
        Log.d(tagDB, "Вызов метода onDestroyView фрагмента KindsFragment");
        super.onDestroyView();
        binding = null;
//        cursor.close();
    }

    /**
     * Метод предоставления и закрытия доступа к компонентам изменения БД
     * @param status
     */
    public void setEnabled(boolean status) {
        binding.btnAdd.setEnabled(status);
        binding.btnEdit.setEnabled(status);
        binding.btnDelete.setEnabled(status);
        if (kindId < 0) {
            binding.btnEdit.setEnabled(false);
            binding.btnDelete.setEnabled(false);
        }
    }

    /**
     * Метод считывания данных из БД
     */
    public void readDB(){
        Log.d(tagDB, "Вызов метода readDB фрагмента KindsFragment");
        database = dbHelper.getReadableDatabase();
        cursor = database.query(DBHelper.TABLE_NAME_KINDS,
                null,
                null,
                null,
                null,
                null,
                null);
        simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_kind , cursor, from, to, 0);
        binding.lvKinds.setAdapter(simpleCursorAdapter);
        binding.lvKinds.setOnItemClickListener(this::onClickList);


        if(cursor.moveToFirst() == true){
            //Получение индексов
            do {
                simpleCursorAdapter.changeCursor(cursor);
                binding.lvKinds.setOnItemClickListener(this::onClickList);
            } while (cursor.moveToNext() == true);
        }
//        cursor.close();
    }

    /**
     * Метод взаимдействия пользователя со списком данных (событие нажатия на элемент списка)
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    public void onClickList(AdapterView<?> parent, View view, int position, long id){
        kindId = (int)id;
        if (getArguments().getString("action").equals("choose")){
            NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            String key = "select_kind";
            Bundle bundle = new Bundle();
            bundle.putInt(key, kindId);
            requireActivity().getSupportFragmentManager().setFragmentResult(key, bundle);
            host.popBackStack();
        }else {
            if (rowIsExist(DBHelper.TABLE_NAME_KINDS, DBHelper.KEY_KINDS_ID, kindId)) {
//            binding.edEnterName.setText(String.valueOf(directionId));
                Cursor item = (Cursor) simpleCursorAdapter.getItem(position);
                binding.etName.setText(item.getString(item.getColumnIndexOrThrow(from[0])));
                setEnabled(true);
            } else {
                setEnabled(false);
            }
        }
    }
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
     * Метод добавления записи в БД
     * @param view
     */
    public void onAdd(View view){
        Log.d(tagDB, "Вызов метода onAdd фрагмента KindsFragment");
        database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_KINDS_NAME, binding.etName.getText().toString());

        long result = database.insert(DBHelper.TABLE_NAME_KINDS, null, contentValues);

        if (result > 0){
            Log.d(tagDB, getResources().getString(R.string.action_result_OK));
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_add_result_OK),Toast.LENGTH_LONG);
            readDB();
        }else{
            Log.d(tagDB, getResources().getString(R.string.action_result_ERROR));
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_ERROR),Toast.LENGTH_LONG);
        }
        toast.show();
    }
    /**
     * Метод изменения выбранной записи данных в БД
     * @param view
     */
    public void onEdit(View view){
        Log.d(tagDB, "Вызов метода onEdit фрагмента KindsFragment");
        database = dbHelper.getWritableDatabase();
        if (rowIsExist(DBHelper.TABLE_NAME_KINDS,DBHelper.KEY_KINDS_ID,kindId)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_KINDS_NAME, binding.etName.getText().toString());
            String where = DBHelper.KEY_KINDS_ID + "=" + kindId;

            long result = database.update(DBHelper.TABLE_NAME_KINDS, contentValues, where, null);
            if (result > 0) {
                Log.d(tagDB, getResources().getString(R.string.action_result_OK));
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_edit_result_OK),Toast.LENGTH_LONG);
                readDB();
            }else {
                Log.d(tagDB, getResources().getString(R.string.action_result_ERROR));
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_ERROR),Toast.LENGTH_LONG);
            }
        }else {
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.db_row_cant_find),Toast.LENGTH_LONG);
            Log.d(tagDB, getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.db_row_cant_find));
        }
        toast.show();
    }
    /**
     * Метод удаления выбранной записи из БД
     * @param view
     */
    public void onDelete(View view){
        Log.d(tagDB, "Вызов метода onDelete фрагмента DirectionFragment");
        database = dbHelper.getWritableDatabase();

        if (rowIsExist(DBHelper.TABLE_NAME_KINDS,DBHelper.KEY_KINDS_ID,kindId)) {
            if (!rowIsExist(DBHelper.TABLE_NAME_TOURS,DBHelper.KEY_TOURS_ID_KIND,kindId)) {
                long result = database.delete(dbHelper.TABLE_NAME_KINDS,
                        DBHelper.KEY_KINDS_ID + " = ?", new String[]{String.valueOf(kindId)});
                if (result > 0) {
                    Log.d(tagDB, getResources().getString(R.string.action_result_OK));
                    toast = Toast.makeText(getContext(), getResources().getString(R.string.action_delete_result_OK), Toast.LENGTH_LONG);
                    readDB();
                } else {
                    Log.d(tagDB, getResources().getString(R.string.action_result_ERROR));
                    toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_ERROR), Toast.LENGTH_LONG);
                }
            }else {
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_NOT_OK)
                        + " - " + getResources().getString(R.string.db_row_in_use),Toast.LENGTH_LONG);
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