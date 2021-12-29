package com.example.cursework.fragments;

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
import com.example.cursework.databinding.FragmentEmployeeBinding;
import com.example.cursework.databinding.FragmentTourOperatorBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Toast toast;
    public static final String tagDB = "tagFragment";
    private FragmentEmployeeBinding binding;
    DBHelper dbHelper;
    SQLiteDatabase database;
    SimpleCursorAdapter simpleCursorAdapter;
    Cursor cursor;
    int employeeID;
    public static String[] from = new String[] {
            DBHelper.KEY_EMPLOYEES_NAME,
            DBHelper.KEY_EMPLOYEES_SURNAME,
            DBHelper.KEY_EMPLOYEES_PATRONYMIC,
            DBHelper.KEY_EMPLOYEES_MAIL
    };
    public static int[] to = new int[]{
            R.id.etName,
            R.id.etSurname,
            R.id.etPatronymic,
            R.id.etMail
    };


    public EmployeeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmployeeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployeeFragment newInstance(String param1, String param2) {
        EmployeeFragment fragment = new EmployeeFragment();
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
        Log.d(tagDB, "Вызов метода onCreateView фрагмента EmployeeFragment");
        // Inflate the layout for this fragment
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        binding = FragmentEmployeeBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        binding.btnAdd.setOnClickListener(this::onAdd);
        binding.btnSearch.setOnClickListener(this::onSearch);
        binding.lvEmployee.setOnItemClickListener(this::onClickList);
        dbHelper = new DBHelper(getContext());
        readDB();
        return root;
//        return inflater.inflate(R.layout.fragment_employee, container, false);
    }

    @Override
    public void onDestroyView() {
        Log.d(tagDB, "Вызов метода onDestroyView фрагмента EmployeeFragment");
        super.onDestroyView();
        binding = null;
//        cursor.close();
    }

    /**
     * Метод считывания данных из БД
     */
    public void readDB(){
        Log.d(tagDB, "Вызов метода readDB фрагмента EmployeeFragment");
        database = dbHelper.getReadableDatabase();
        cursor = database.query(DBHelper.TABLE_NAME_EMPLOYEES,
                null,
                null,
                null,
                null,
                null,
                null);
        simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_employee , cursor, from, to, 0);
        binding.lvEmployee.setAdapter(simpleCursorAdapter);
        binding.lvEmployee.setOnItemClickListener(this::onClickList);


        if(cursor.moveToFirst() == true){
            //Получение индексов
            do {
                simpleCursorAdapter.changeCursor(cursor);
                binding.lvEmployee.setOnItemClickListener(this::onClickList);
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
        if (Authorisation.id == id) {
            Bundle bundle = new Bundle();
            bundle.putInt("id", (int) id);
            NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            host.navigate(R.id.fragment_employee_detail, bundle);
        }
    }
    /**
     * Метод для создания новой записи
     * @param view
     */
    public void onAdd(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("id", -1);
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_employee_detail,bundle);
    }

    /**
     * Метод для поиска туроператоров по заданному наименованию и уникальному номеру
     * @param view
     */
    public void onSearch(View view){
        String enteredMail = binding.etMail.getText().toString();
        database = dbHelper.getReadableDatabase();
        cursor = database.query(DBHelper.TABLE_NAME_EMPLOYEES,
                null,
                DBHelper.KEY_EMPLOYEES_MAIL + " LIKE  ?",
                new String[] {
                        enteredMail + "%"},
                null,
                null,
                null);
        if(cursor.moveToFirst() == true) {
//            int indId = cursor.getColumnIndex(DBHelper.KEY_TOUR_OPERATORS_ID);
//            employeeID = cursor.getInt(indId);
            {
                simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_employee , cursor, from, to, 0);
                binding.lvEmployee.setAdapter(simpleCursorAdapter);
                binding.lvEmployee.setOnItemClickListener(this::onClickList);
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_search_OK,cursor.getCount()),Toast.LENGTH_LONG);
                Log.d(tagDB, getResources().getString(R.string.action_search_OK,cursor.getCount()));
                if(cursor.moveToFirst() == true){
                    //Получение индексов
                    do {
                        simpleCursorAdapter.changeCursor(cursor);
                        binding.lvEmployee.setOnItemClickListener(this::onClickList);
                    } while (cursor.moveToNext() == true);
                }
            }
        }else {
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_search_null),Toast.LENGTH_LONG);
            Log.d(tagDB, getResources().getString(R.string.action_search_null));
        }
        toast.show();
    }
}