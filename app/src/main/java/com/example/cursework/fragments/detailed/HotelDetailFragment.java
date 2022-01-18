package com.example.cursework.fragments.detailed;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.cursework.Authorisation;
import com.example.cursework.R;
import com.example.cursework.dataBase.DBHelper;
import com.example.cursework.databinding.FragmentHotelDetailBinding;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HotelDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HotelDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Toast toast;
    public static final String tagDB = "tagFragment";
    private FragmentHotelDetailBinding binding;
    DBHelper dbHelper;
    SQLiteDatabase database;
    Cursor cursor;
    int selectedDirectionId;
    int selectedHotelId;
    int[] directionIds;
    String[] directionNames = {"0"};

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HotelDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HotelDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HotelDetailFragment newInstance(String param1, String param2) {
        HotelDetailFragment fragment = new HotelDetailFragment();
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
        binding = FragmentHotelDetailBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        binding.btnAdd.setOnClickListener(this::onAdd);
        binding.btnEdit.setOnClickListener(this::onEdit);
        binding.btnDelete.setOnClickListener(this::onDelete);

        dbHelper = new DBHelper(getContext());
        readDbDirections();

        selectedHotelId = getArguments().getInt("id");
        if (selectedHotelId >= 0) {
            readDbHotel();
        }
        if (Authorisation.isLoggedIn) setEnabled(true);
        else  setEnabled(false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, directionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnDirection.setAdapter(adapter);
        binding.spnDirection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDirectionId = (int)id+1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (selectedHotelId > 0) {
            for (int i=0;i<directionIds.length;i++){
                if (directionIds[i] == selectedDirectionId){
                    binding.spnDirection.setSelection(i);
                    break;
                }
            }
        }

        return root;
//        return inflater.inflate(R.layout.fragment_hotel_detail, container, false);
    }
    /**
     * Метод предоставления и закрытия доступа к компонентам изменения БД
     * @param status
     */
    public void setEnabled(boolean status) {
        binding.btnAdd.setEnabled(status);
        binding.btnEdit.setEnabled(status);
        binding.btnDelete.setEnabled(status);
        if (selectedHotelId < 0) {
            binding.btnEdit.setEnabled(false);
            binding.btnDelete.setEnabled(false);
        }
    }

    /**
     * Метод считывания записей  Направлений из БД
     */
    public void readDbDirections(){
        Log.d(tagDB, "Вызов метода readDbDirections фрагмента HotelDetailFragment");
        database = dbHelper.getReadableDatabase();
        cursor = database.query(DBHelper.TABLE_NAME_DIRECTIONS,
                null,
                null,
                null,
                null,
                null,
                null);

        if(cursor.moveToFirst() == true){
            int count = cursor.getCount();
            directionIds = new int[count];
            directionNames = new String[count];
            int i =0;
            //Получение индексов
            do {
                int indID = cursor.getColumnIndex(DBHelper.KEY_DIRECTIONS_ID);
                directionIds[i] = cursor.getInt(indID);
                int indName = cursor.getColumnIndex(DBHelper.KEY_DIRECTIONS_NAME);
                directionNames[i] = cursor.getString(indName);
//                if (selectedDirectionId > 0 && selectedDirectionId == directionIds[i]){
//                    binding.spnDirection.setSelection(i);
//                }
                i++;
            } while (cursor.moveToNext() == true);
        }
//        cursor.close();
    }
    /**
     * Метод считывания записи Отеля из БД
     */
    public void readDbHotel() {
        Log.d(tagDB, "Вызов метода readDbHotel фрагмента HotelDetailFragment");
        database = dbHelper.getReadableDatabase();
        cursor = database.query(DBHelper.TABLE_NAME_HOTELS,
                null,
                DBHelper.KEY_HOTELS_ID + " = ?",
                new String[]{
                        String.valueOf(selectedHotelId)},
                null,
                null,
                null);

        if (cursor.moveToFirst() == true) {
            int indID = cursor.getColumnIndex(DBHelper.KEY_HOTELS_ID);
            selectedHotelId = cursor.getInt(indID);
            int indName = cursor.getColumnIndex(DBHelper.KEY_HOTELS_NAME);
            binding.etHotelName.setText(cursor.getString(indName));
            int indAddress = cursor.getColumnIndex(DBHelper.KEY_HOTELS_ADDRESS);
            binding.etHotelAddress.setText(cursor.getString(indAddress));
            int indDirection = cursor.getColumnIndex(DBHelper.KEY_HOTELS_ID_DIRECTION);
            selectedDirectionId = cursor.getInt(indDirection);
            readDbDirections();
        }
//        cursor.close();
    }
    /**
     * Проверка наличия выбранной записи данных в БД
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
     * Метод добавления записи в БД
     * @param view
     */
    public void onAdd(View view){
        Log.d(tagDB, "Вызов метода onAdd фрагмента HotelDetailFragment");
        database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_HOTELS_NAME, binding.etHotelName.getText().toString());
        contentValues.put(DBHelper.KEY_HOTELS_ADDRESS, binding.etHotelAddress.getText().toString());
        contentValues.put(DBHelper.KEY_HOTELS_ID_DIRECTION, String.valueOf(selectedDirectionId));

        long result = database.insert(DBHelper.TABLE_NAME_HOTELS, null, contentValues);
            if (result > 0) {
                Log.d(tagDB, getResources().getString(R.string.action_result_OK));
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_add_result_OK), Toast.LENGTH_LONG);
            } else {
                Log.d(tagDB, getResources().getString(R.string.action_result_ERROR));
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_ERROR), Toast.LENGTH_LONG);
            }
        toast.show();
    }

    /**
     * Метод изменения выбранной записи данных в БД
     * @param view
     */
    public void onEdit(View view){
        Log.d(tagDB, "Вызов метода onEdit фрагмента HotelDetailFragment");
        database = dbHelper.getWritableDatabase();
        if (rowIsExist(DBHelper.TABLE_NAME_DIRECTIONS, DBHelper.KEY_DIRECTIONS_ID, selectedDirectionId)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_HOTELS_NAME, binding.etHotelName.getText().toString());
            contentValues.put(DBHelper.KEY_HOTELS_ADDRESS, binding.etHotelAddress.getText().toString());
            contentValues.put(DBHelper.KEY_HOTELS_ID_DIRECTION, String.valueOf(selectedDirectionId));
            String where = DBHelper.KEY_DIRECTIONS_ID + "=" + selectedHotelId;

            long result = database.update(DBHelper.TABLE_NAME_HOTELS, contentValues, where, null);
            if (result > 0) {
                Log.d(tagDB, getResources().getString(R.string.action_result_OK));
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_edit_result_OK),Toast.LENGTH_LONG);
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
        Log.d(tagDB, "Вызов метода onDelete фрагмента HotelDetailFragment");
        database = dbHelper.getWritableDatabase();

        if (rowIsExist(DBHelper.TABLE_NAME_HOTELS, DBHelper.KEY_HOTELS_ID, selectedHotelId)) {
            if  (!rowIsExist(DBHelper.TABLE_NAME_TOURS, DBHelper.KEY_TOURS_ID_HOTEL, selectedHotelId)) {
                long result = database.delete(dbHelper.TABLE_NAME_HOTELS,
                        DBHelper.KEY_HOTELS_ID + " = ?", new String[]{String.valueOf(selectedHotelId)});
                if (result > 0) {
                    Log.d(tagDB, getResources().getString(R.string.action_result_OK));
                    toast = Toast.makeText(getContext(), getResources().getString(R.string.action_delete_result_OK), Toast.LENGTH_LONG);
                } else {
                    Log.d(tagDB, getResources().getString(R.string.action_result_ERROR));
                    toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_ERROR), Toast.LENGTH_LONG);
                }
            }else {
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