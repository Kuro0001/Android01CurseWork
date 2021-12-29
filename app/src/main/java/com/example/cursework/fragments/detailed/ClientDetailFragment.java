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
import android.widget.Toast;

import com.example.cursework.Authorisation;
import com.example.cursework.R;
import com.example.cursework.dataBase.DBHelper;
import com.example.cursework.databinding.FragmentClientDetailBinding;
import com.example.cursework.databinding.FragmentTourOperatorDetailBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Toast toast;
    public static final String tagDB = "tagFragment";
    private FragmentClientDetailBinding binding;
    DBHelper dbHelper;
    SQLiteDatabase database;
    Cursor cursor;
    int selectedID;
    boolean sex = false;

    public ClientDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClientDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientDetailFragment newInstance(String param1, String param2) {
        ClientDetailFragment fragment = new ClientDetailFragment();
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
        binding = FragmentClientDetailBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        binding.btnAdd.setOnClickListener(this::onAdd);
        binding.btnEdit.setOnClickListener(this::onEdit);
        binding.btnDelete.setOnClickListener(this::onDelete);
        binding.cbSexMale.setOnClickListener(this::onMale);
        binding.cbSexFemale.setOnClickListener(this::onFemale);

        dbHelper = new DBHelper(getContext());

        selectedID = getArguments().getInt("id");
        if (selectedID >= 0) {
            setEnabled(true);
            readDb();
        } else {
            setEnabled(false);
        }
        return root;
//        return inflater.inflate(R.layout.fragment_client_detail, container, false);
    }

    /**
     * Метод предоставления и закрытия доступа к компонентам изменения БД
     *
     * @param status
     */
    public void setEnabled(boolean status){
        if (Authorisation.isLoggedIn) {
            binding.btnAdd.setEnabled(status);
            binding.btnEdit.setEnabled(status);
            binding.btnDelete.setEnabled(status);
        }
    }

    /**
     * Метод установки значения для компонентов Check box
     *
     * @param sex
     */
    public void setSex(boolean sex) {
        binding.cbSexMale.setChecked(false);
        binding.cbSexFemale.setChecked(false);
        if (sex) {
            binding.cbSexMale.setChecked(true);
            sex = true;
        } else {
            binding.cbSexFemale.setChecked(true);
            sex = false;
        }
    }

    /**
     * Проверка корректно указанного пола, т.е. ровно одного
     * @return
     */
    public boolean isSexCorrect() {
        if (binding.cbSexMale.isChecked() != binding.cbSexFemale.isChecked()) {
            return true;
        }
        return false;
    }

    /**
     * Метод считывания записи Клиента из БД
     */
    public void readDb() {
        Log.d(tagDB, "Вызов метода readDb фрагмента ClientDetailFragment");
        database = dbHelper.getReadableDatabase();
        cursor = database.query(DBHelper.TABLE_NAME_CLIENTS,
                null,
                DBHelper.KEY_CLIENTS_ID + " = ?",
                new String[]{
                        String.valueOf(selectedID)},
                null,
                null,
                null);

        if (cursor.moveToFirst() == true) {
            int indID = cursor.getColumnIndex(DBHelper.KEY_CLIENTS_ID);
            selectedID = cursor.getInt(indID);
            int indName = cursor.getColumnIndex(DBHelper.KEY_CLIENTS_NAME);
            binding.etName.setText(cursor.getString(indName));
            int indSurname = cursor.getColumnIndex(DBHelper.KEY_CLIENTS_SURNAME);
            binding.etSurname.setText(cursor.getString(indSurname));
            int indPatronymic = cursor.getColumnIndex(DBHelper.KEY_CLIENTS_PATRONYMIC);
            binding.etPatronymic.setText(cursor.getString(indPatronymic));
            int indPassport = cursor.getColumnIndex(DBHelper.KEY_CLIENTS_PASSPORT);
            binding.etPassport.setText(cursor.getString(indPassport));
            int indSex = cursor.getColumnIndex(DBHelper.KEY_CLIENTS_SEX);
            String sex = cursor.getString(indSex);
            if (sex.equals("м")) {
                setSex(true);
            } else {
                setSex(false);
            }
            int indDate = cursor.getColumnIndex(DBHelper.KEY_CLIENTS_BIRTHDATE);
            binding.etBirthDate.setText(cursor.getString(indDate));
            int indPhone = cursor.getColumnIndex(DBHelper.KEY_CLIENTS_PHONE);
            binding.etPhone.setText(cursor.getString(indPhone));
            int indMale = cursor.getColumnIndex(DBHelper.KEY_CLIENTS_MAIL);
            binding.etMail.setText(cursor.getString(indMale));
        }
//        cursor.close();
    }

    /**
     * Проверка наличия выбранной записи данных в БД
     *
     * @param id
     * @return
     */
    public boolean rowIsExist(String table, String columnName, int id) {
        database = dbHelper.getReadableDatabase();
        cursor = database.query(table,
                null,
                columnName + " LIKE ?",
                new String[]{
                        id + "%"},
                null,
                null,
                null);
        if (cursor.moveToFirst() == true)
            return true;
        return false;
    }

    /**
     * Метод определения пола как мужской
     * @param view
     */
    public void onMale(View view) {
        setSex(true);
    }
    /**
     * Метод определения пола как женский
     * @param view
     */
    public void onFemale(View view) {
        setSex(false);
    }

    /**
     * Метод добавления записи в БД
     *
     * @param view
     */
    public void onAdd(View view) {
        Log.d(tagDB, "Вызов метода onAdd фрагмента ClientDetailFragment");
        database = dbHelper.getWritableDatabase();
        if (isSexCorrect()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_CLIENTS_NAME, binding.etName.getText().toString());
            contentValues.put(DBHelper.KEY_CLIENTS_SURNAME, binding.etSurname.getText().toString());
            contentValues.put(DBHelper.KEY_CLIENTS_PATRONYMIC, binding.etPatronymic.getText().toString());
            contentValues.put(DBHelper.KEY_CLIENTS_PASSPORT, binding.etPassport.getText().toString());
            String s;
            if (sex){
                s = "м";
            } else {
                s = "ж";
            }
            contentValues.put(DBHelper.KEY_CLIENTS_SEX, s);
            contentValues.put(DBHelper.KEY_CLIENTS_BIRTHDATE, binding.etBirthDate.getText().toString());
            contentValues.put(DBHelper.KEY_CLIENTS_PHONE, binding.etPhone.getText().toString());
            contentValues.put(DBHelper.KEY_CLIENTS_MAIL, binding.etMail.getText().toString());

            long result = database.insert(DBHelper.TABLE_NAME_CLIENTS, null, contentValues);
            if (result > 0) {
                Log.d(tagDB, getResources().getString(R.string.action_result_OK));
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_add_result_OK), Toast.LENGTH_LONG);
            } else {
                Log.d(tagDB, getResources().getString(R.string.action_result_ERROR));
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_ERROR), Toast.LENGTH_LONG);
            }
        }else{
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct), Toast.LENGTH_LONG);
            Log.d(tagDB, getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct));
        }
        toast.show();
    }

    /**
     * Метод изменения выбранной записи данных в БД
     *
     * @param view
     */
    public void onEdit(View view) {
        Log.d(tagDB, "Вызов метода onEdit фрагмента ClientDetailFragment");
        database = dbHelper.getWritableDatabase();
        if (isSexCorrect()) {
            if (rowIsExist(DBHelper.TABLE_NAME_CLIENTS, DBHelper.KEY_CLIENTS_ID, selectedID)) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_CLIENTS_NAME, binding.etName.getText().toString());
                contentValues.put(DBHelper.KEY_CLIENTS_SURNAME, binding.etSurname.getText().toString());
                contentValues.put(DBHelper.KEY_CLIENTS_PATRONYMIC, binding.etPatronymic.getText().toString());
                contentValues.put(DBHelper.KEY_CLIENTS_PASSPORT, binding.etPassport.getText().toString());
                String s;
                if (sex){
                    s = "м";
                } else {
                    s = "ж";
                }
                contentValues.put(DBHelper.KEY_CLIENTS_SEX, s);
                contentValues.put(DBHelper.KEY_CLIENTS_BIRTHDATE, binding.etBirthDate.getText().toString());
                contentValues.put(DBHelper.KEY_CLIENTS_PHONE, binding.etPhone.getText().toString());
                contentValues.put(DBHelper.KEY_CLIENTS_MAIL, binding.etMail.getText().toString());
                String where = DBHelper.KEY_CLIENTS_ID + "=" + selectedID;

                long result = database.update(DBHelper.TABLE_NAME_CLIENTS, contentValues, where, null);
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
        }else{
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct), Toast.LENGTH_LONG);
            Log.d(tagDB, getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct));
        }
        toast.show();
    }

    /**
     * Метод удаления выбранной записи из БД
     *
     * @param view
     */
    public void onDelete(View view) {
        Log.d(tagDB, "Вызов метода onDelete фрагмента ClientDetailFragment");
        database = dbHelper.getWritableDatabase();

        if (rowIsExist(DBHelper.TABLE_NAME_CLIENTS, DBHelper.KEY_CLIENTS_ID, selectedID)) {
            if (!rowIsExist(DBHelper.TABLE_NAME_VOUCHERS, DBHelper.KEY_VOUCHERS_ID_CLIENT, selectedID)) {
                long result = database.delete(dbHelper.TABLE_NAME_CLIENTS,
                        DBHelper.KEY_CLIENTS_ID + " = ?", new String[]{String.valueOf(selectedID)});
                if (result > 0) {
                    Log.d(tagDB, getResources().getString(R.string.action_result_OK));
                    toast = Toast.makeText(getContext(), getResources().getString(R.string.action_delete_result_OK), Toast.LENGTH_LONG);
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
        } else {
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.db_row_cant_find), Toast.LENGTH_LONG);
            Log.d(tagDB, getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.db_row_cant_find));
        }
        toast.show();
    }
}