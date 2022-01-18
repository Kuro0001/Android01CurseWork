package com.example.cursework.fragments.detailed;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.cursework.Authorisation;
import com.example.cursework.R;
import com.example.cursework.Validation;
import com.example.cursework.dataBase.DBHelper;
import com.example.cursework.databinding.FragmentHotelDetailBinding;
import com.example.cursework.databinding.FragmentTourOperatorDetailBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TourOperatorDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TourOperatorDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Toast toast;
    public static final String tagDB = "tagFragment";
    private FragmentTourOperatorDetailBinding binding;
    DBHelper dbHelper;
    SQLiteDatabase database;
    Cursor cursor;
    int selectedTourOperatorID;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TourOperatorDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TourOperatorDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TourOperatorDetailFragment newInstance(String param1, String param2) {
        TourOperatorDetailFragment fragment = new TourOperatorDetailFragment();
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
        binding = FragmentTourOperatorDetailBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        binding.btnAdd.setOnClickListener(this::onAdd);
        binding.btnEdit.setOnClickListener(this::onEdit);
        binding.btnDelete.setOnClickListener(this::onDelete);

        dbHelper = new DBHelper(getContext());


        selectedTourOperatorID = getArguments().getInt("id");
        if (selectedTourOperatorID >= 0) {
            readDb();
        }
        if (Authorisation.isLoggedIn) setEnabled(true);
        else  setEnabled(false);
        return root;
//        return inflater.inflate(R.layout.fragment_tour_operator_detail, container, false);
    }

    /**
     * Метод предоставления и закрытия доступа к компонентам изменения БД
     * @param status
     */
    public void setEnabled(boolean status){
            binding.btnAdd.setEnabled(status);
            binding.btnEdit.setEnabled(status);
            binding.btnDelete.setEnabled(status);
        if (selectedTourOperatorID < 0){
            binding.btnEdit.setEnabled(false);
            binding.btnDelete.setEnabled(false);
        }
    }

    /**
     * Метод считывания записи Отеля из БД
     */
    public void readDb() {
        Log.d(tagDB, "Вызов метода readDb фрагмента TourOperatorDetailFragment");
        database = dbHelper.getReadableDatabase();
        cursor = database.query(DBHelper.TABLE_NAME_TOUR_OPERATORS,
                null,
                DBHelper.KEY_TOUR_OPERATORS_ID + " = ?",
                new String[]{
                        String.valueOf(selectedTourOperatorID)},
                null,
                null,
                null);

        if (cursor.moveToFirst() == true) {
            int indID = cursor.getColumnIndex(DBHelper.KEY_TOUR_OPERATORS_ID);
            selectedTourOperatorID = cursor.getInt(indID);
            int indName = cursor.getColumnIndex(DBHelper.KEY_TOUR_OPERATORS_NAME);
            binding.etName.setText(cursor.getString(indName));
            int indPhone = cursor.getColumnIndex(DBHelper.KEY_TOUR_OPERATORS_PHONE);
            binding.etPhone.setText(cursor.getString(indPhone));
            int indUniqNumber = cursor.getColumnIndex(DBHelper.KEY_TOUR_OPERATORS_UNIQUE_COMPANY_NUMBER);
            binding.etUniqueNumber.setText(cursor.getString(indUniqNumber));
            int indMail = cursor.getColumnIndex(DBHelper.KEY_TOUR_OPERATORS_MAIL);
            binding.etMail.setText(cursor.getString(indMail));
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
     * Метод для проверки введеных данных пользователем
     * @return
     */
    public boolean isCorrectInput(){
        if(!Validation.isRightName(binding.etName.getText().toString())) return false;
        if(!Validation.isRightPhone(binding.etPhone.getText().toString())) return false;
        if(!Validation.isRightCompanyNumber(binding.etUniqueNumber.getText().toString())) return false;
        if(!Validation.isRightEmail(binding.etMail.getText().toString())) return false;
        return true;
    }

    /**
     * Метод добавления записи в БД
     * @param view
     */
    public void onAdd(View view) {
        Log.d(tagDB, "Вызов метода onAdd фрагмента TourOperatorDetailFragment");
        database = dbHelper.getWritableDatabase();
        if(!isCorrectInput()) {
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct), Toast.LENGTH_LONG);
            Log.d(tagDB, getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct));
        }
        else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_TOUR_OPERATORS_NAME, binding.etName.getText().toString());
            contentValues.put(DBHelper.KEY_TOUR_OPERATORS_PHONE, binding.etPhone.getText().toString());
            contentValues.put(DBHelper.KEY_TOUR_OPERATORS_UNIQUE_COMPANY_NUMBER, binding.etUniqueNumber.getText().toString());
            contentValues.put(DBHelper.KEY_TOUR_OPERATORS_MAIL, binding.etMail.getText().toString());

            long result = database.insert(DBHelper.TABLE_NAME_TOUR_OPERATORS, null, contentValues);
            if (result > 0) {
                Log.d(tagDB, getResources().getString(R.string.action_result_OK));
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_add_result_OK), Toast.LENGTH_LONG);
            } else {
                Log.d(tagDB, getResources().getString(R.string.action_result_ERROR));
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_ERROR), Toast.LENGTH_LONG);
            }
        }
        toast.show();
    }

    /**
     * Метод изменения выбранной записи данных в БД
     * @param view
     */
    public void onEdit(View view){
        Log.d(tagDB, "Вызов метода onEdit фрагмента TourOperatorDetailFragment");
        database = dbHelper.getWritableDatabase();
        if(!isCorrectInput()) {
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct), Toast.LENGTH_LONG);
            Log.d(tagDB, getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct));
        }
        else {
            if (rowIsExist(DBHelper.TABLE_NAME_TOUR_OPERATORS, DBHelper.KEY_TOUR_OPERATORS_ID, selectedTourOperatorID)) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_TOUR_OPERATORS_NAME, binding.etName.getText().toString());
                contentValues.put(DBHelper.KEY_TOUR_OPERATORS_PHONE, binding.etPhone.getText().toString());
                contentValues.put(DBHelper.KEY_TOUR_OPERATORS_UNIQUE_COMPANY_NUMBER, binding.etUniqueNumber.getText().toString());
                contentValues.put(DBHelper.KEY_TOUR_OPERATORS_MAIL, binding.etMail.getText().toString());
                String where = DBHelper.KEY_TOUR_OPERATORS_ID + "=" + selectedTourOperatorID;

                long result = database.update(DBHelper.TABLE_NAME_TOUR_OPERATORS, contentValues, where, null);
                if (result > 0) {
                    Log.d(tagDB, getResources().getString(R.string.action_result_OK));
                    toast = Toast.makeText(getContext(), getResources().getString(R.string.action_edit_result_OK), Toast.LENGTH_LONG);
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
     * Метод удаления выбранной записи из БД
     * @param view
     */
    public void onDelete(View view){
        Log.d(tagDB, "Вызов метода onDelete фрагмента TourOperatorDetailFragment");
        database = dbHelper.getWritableDatabase();

        if (rowIsExist(DBHelper.TABLE_NAME_TOUR_OPERATORS, DBHelper.KEY_TOUR_OPERATORS_ID, selectedTourOperatorID)) {
            if  (!rowIsExist(DBHelper.TABLE_NAME_TOURS, DBHelper.KEY_TOURS_ID_TOUR_OPERATOR, selectedTourOperatorID)) {
                long result = database.delete(dbHelper.TABLE_NAME_TOUR_OPERATORS,
                        DBHelper.KEY_TOUR_OPERATORS_ID + " = ?", new String[]{String.valueOf(selectedTourOperatorID)});
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