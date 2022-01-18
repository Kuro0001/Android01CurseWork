package com.example.cursework.fragments.detailed;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.transition.Visibility;

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
import com.example.cursework.databinding.FragmentEmployeeBinding;
import com.example.cursework.databinding.FragmentEmployeeDetailBinding;
import com.example.cursework.databinding.FragmentTourOperatorDetailBinding;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Toast toast;
    public static final String tagDB = "tagFragment";
    private FragmentEmployeeDetailBinding binding;
    DBHelper dbHelper;
    SQLiteDatabase database;
    Cursor cursor;
    int selectedId;
    String[] months = new String[12];
    Calendar calendar = Calendar.getInstance();
    int currentYear = calendar.get(Calendar.YEAR);
    String[] years = new String[50];
    int startMonth;
    int startYear;
    int endMonth;
    int endYear;

    public EmployeeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmployeeDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployeeDetailFragment newInstance(String param1, String param2) {
        EmployeeDetailFragment fragment = new EmployeeDetailFragment();
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
        binding = FragmentEmployeeDetailBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        binding.btnAdd.setOnClickListener(this::onAdd);
        binding.btnEdit.setOnClickListener(this::onEdit);
        binding.btnDelete.setOnClickListener(this::onDelete);
        binding.btnOnResuts.setOnClickListener(this::onResults);
        dbHelper = new DBHelper(getContext());
        selectedId = getArguments().getInt("id");
        if (selectedId >= 0) {
            readDb();
            setSpinners();
        }
        if (Authorisation.isLoggedIn) setEnabled(true);
        else setEnabled(false);
        return root;
//        return inflater.inflate(R.layout.fragment_employee_detail, container, false);
    }
    /**
     * Метод заполнения спиннеров даты для их далнейшей эксплуатации
     */
    public void setSpinners() {
        Log.d(tagDB, "Вызов метода setSpinners фрагмента EmployeeDetailFragment");
        for (int i = 1; i < 10; i++) {
            months[i - 1] = 0 + String.valueOf(i);
        }
        for (int i = 10; i < 13; i++) {
            months[i - 1] = String.valueOf(i);
        }
        for (int i = 0; i < 50; i++) {
            years[i] = String.valueOf(2000 + i);
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, months);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnDateStartMonth.setAdapter(adapter1);
        binding.spnDateStartMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startMonth = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, years);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnDateStartYear.setAdapter(adapter2);
        binding.spnDateStartYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startYear = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, months);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnDateEndMonth.setAdapter(adapter3);
        binding.spnDateEndMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                endMonth = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, years);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnDateEndYear.setAdapter(adapter4);
        binding.spnDateEndYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                endYear = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.spnDateStartMonth.setSelection(calendar.get(Calendar.MONTH));
        binding.spnDateStartYear.setSelection(currentYear - 2000);
        binding.spnDateEndMonth.setSelection(calendar.get(Calendar.MONTH));
        binding.spnDateEndYear.setSelection(currentYear - 2000);
    }
    /**
     * Метод предоставления и закрытия доступа к компонентам изменения БД
     * @param status
     */
    public void setEnabled(boolean status) {
        binding.btnAdd.setEnabled(status);
        binding.btnEdit.setEnabled(status);
        binding.btnDelete.setEnabled(status);

        if (selectedId < 0) {
            binding.btnEdit.setEnabled(false);
            binding.btnDelete.setEnabled(false);
            binding.llResults.setVisibility(View.INVISIBLE);
            binding.llResults.setVisibility(View.GONE);
        }
    }

    public void readDb() {
        Log.d(tagDB, "Вызов метода readDb фрагмента EmployeeDetailFragment");
        database = dbHelper.getReadableDatabase();
        cursor = database.query(DBHelper.TABLE_NAME_EMPLOYEES,
                null,
                DBHelper.KEY_EMPLOYEES_ID + " = ?",
                new String[]{
                        String.valueOf(selectedId)},
                null,
                null,
                null);

        if (cursor.moveToFirst() == true) {
            int indID = cursor.getColumnIndex(DBHelper.KEY_EMPLOYEES_ID);
            selectedId = cursor.getInt(indID);
            int indName = cursor.getColumnIndex(DBHelper.KEY_EMPLOYEES_NAME);
            binding.etName.setText(cursor.getString(indName));
            int indSurname = cursor.getColumnIndex(DBHelper.KEY_EMPLOYEES_SURNAME);
            binding.etSurname.setText(cursor.getString(indSurname));
            int indPatronymic = cursor.getColumnIndex(DBHelper.KEY_EMPLOYEES_PATRONYMIC);
            binding.etPatronymic.setText(cursor.getString(indPatronymic));
            int indMail = cursor.getColumnIndex(DBHelper.KEY_EMPLOYEES_MAIL);
            binding.etMail.setText(cursor.getString(indMail));
            int indLogin = cursor.getColumnIndex(DBHelper.KEY_EMPLOYEES_LOGIN);
            binding.etLogin.setText(cursor.getString(indLogin));
            int indPassword = cursor.getColumnIndex(DBHelper.KEY_EMPLOYEES_PASSWORD);
            binding.etPassword.setText(cursor.getString(indPassword));
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
        if(!Validation.isRightName(binding.etPatronymic.getText().toString())) return false;
        if(!Validation.isRightName(binding.etSurname.getText().toString())) return false;
        if(!Validation.isRightEmail(binding.etMail.getText().toString())) return false;
        if(!Validation.isStrongPassword(binding.etPassword.getText().toString())) return false;
        if(!Validation.isStrongLogin(binding.etLogin.getText().toString())) return false;
        return true;
    }

    /**
     * Метод добавления записи в БД
     * @param view
     */
    public void onAdd(View view) {
        Log.d(tagDB, "Вызов метода onAdd фрагмента EmployeeDetailFragment");
        database = dbHelper.getWritableDatabase();
        if(!isCorrectInput()) {
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct), Toast.LENGTH_LONG);
            Log.d(tagDB, getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct));
        }
        else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_EMPLOYEES_NAME, binding.etName.getText().toString());
            contentValues.put(DBHelper.KEY_EMPLOYEES_SURNAME, binding.etSurname.getText().toString());
            contentValues.put(DBHelper.KEY_EMPLOYEES_PATRONYMIC, binding.etPatronymic.getText().toString());
            contentValues.put(DBHelper.KEY_EMPLOYEES_MAIL, binding.etMail.getText().toString());
            contentValues.put(DBHelper.KEY_EMPLOYEES_LOGIN, binding.etLogin.getText().toString());
            contentValues.put(DBHelper.KEY_EMPLOYEES_PASSWORD, binding.etPassword.getText().toString());

            long result = database.insert(DBHelper.TABLE_NAME_EMPLOYEES, null, contentValues);
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
        Log.d(tagDB, "Вызов метода onEdit фрагмента EmployeeDetailFragment");
        database = dbHelper.getWritableDatabase();
        if(!isCorrectInput()) {
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct), Toast.LENGTH_LONG);
            Log.d(tagDB, getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct));
        }
        else {
            if (rowIsExist(DBHelper.TABLE_NAME_EMPLOYEES, DBHelper.KEY_EMPLOYEES_ID, selectedId)) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_EMPLOYEES_NAME, binding.etName.getText().toString());
                contentValues.put(DBHelper.KEY_EMPLOYEES_SURNAME, binding.etSurname.getText().toString());
                contentValues.put(DBHelper.KEY_EMPLOYEES_PATRONYMIC, binding.etPatronymic.getText().toString());
                contentValues.put(DBHelper.KEY_EMPLOYEES_MAIL, binding.etMail.getText().toString());
                contentValues.put(DBHelper.KEY_EMPLOYEES_LOGIN, binding.etLogin.getText().toString());
                contentValues.put(DBHelper.KEY_EMPLOYEES_PASSWORD, binding.etPassword.getText().toString());
                String where = DBHelper.KEY_EMPLOYEES_ID + "=" + selectedId;

                long result = database.update(DBHelper.TABLE_NAME_EMPLOYEES, contentValues, where, null);
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
        Log.d(tagDB, "Вызов метода onDelete фрагмента EmployeeDetailFragment");
        database = dbHelper.getWritableDatabase();

        if (rowIsExist(DBHelper.TABLE_NAME_EMPLOYEES, DBHelper.KEY_EMPLOYEES_ID, selectedId)) {
            if  (!rowIsExist(DBHelper.TABLE_NAME_VOUCHERS, DBHelper.KEY_VOUCHERS_ID_EMPLOYEE, selectedId)) {
                long result = database.delete(dbHelper.TABLE_NAME_EMPLOYEES,
                        DBHelper.KEY_EMPLOYEES_ID + " = ?", new String[]{String.valueOf(selectedId)});
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

    /**
     * Метод для посчета результата работы сотрудника в указанном временном промежутке
     * @param view
     */
    public void onResults(View view) {
        Log.d(tagDB, "Вызов метода onResults фрагмента EmployeeDetailFragment");

        database = dbHelper.getWritableDatabase();
        Cursor cursor1;
        String dateStart = years[startYear] + months[startMonth] + 00;
        String dateEnd = years[endYear] + months[endMonth] + 99;
        String query = "SELECT COUNT(*) as vouchers_count, SUM(" + DBHelper.KEY_VOUCHERS_PRICE + ") as vouchers_cash " +
                " FROM " + DBHelper.TABLE_NAME_VOUCHERS +
                " WHERE " + DBHelper.KEY_VOUCHERS_ID_EMPLOYEE +
                " = " + selectedId + " AND " + DBHelper.KEY_VOUCHERS_DATE +
                " >= " + dateStart + " AND " + DBHelper.KEY_VOUCHERS_DATE +
                " <= " + dateEnd;
        cursor1 = database.rawQuery(query, null);
        if (cursor1.moveToFirst()) {
            int indCount = cursor1.getColumnIndex("vouchers_count");
            binding.etResultsVouchersCount.setText(cursor1.getString(indCount));
            int indSum = cursor1.getColumnIndex("vouchers_cash");
            binding.etResultsMoney.setText(cursor1.getString(indSum));
        }
    }
}