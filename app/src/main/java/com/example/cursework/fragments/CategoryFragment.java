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
import com.example.cursework.dataBase.DBHelper;
import com.example.cursework.databinding.FragmentCategoryBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Toast toast;
    public static final String tagDB = "tagFragment";
    private FragmentCategoryBinding binding;

    DBHelper dbHelper;
    SQLiteDatabase database;
    SimpleCursorAdapter simpleCursorAdapter;
    Cursor cursor;
    int categoryID;

    public static String[] from = new String[] {
            DBHelper.KEY_CATEGORIES_NAME,
            DBHelper.KEY_CATEGORIES_ADDED_VALUE,
            DBHelper.KEY_CATEGORIES_DISCOUNT
    };
    public static int[] to = new int[]{
            R.id.etName,
            R.id.etAddedValue,
            R.id.etDiscount
    };

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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
        binding = FragmentCategoryBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        binding.btnAdd.setOnClickListener(this::onAdd);
        binding.btnEdit.setOnClickListener(this::onEdit);
        binding.btnDelete.setOnClickListener(this::onDelete);
        binding.lvCategories.setOnItemClickListener(this::onClickList);
        if (Authorisation.isLoggedIn) setEnabled(true);
        else  setEnabled(false);
        dbHelper = new DBHelper(getContext());
        readDB();
        return root;
//        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onDestroyView() {
        Log.d(tagDB, "Вызов метода onDestroyView фрагмента CategoryFragment");
        super.onDestroyView();
        binding = null;
//        cursor.close();
    }

    /**
     * Метод предоставления и закрытия доступа к компонентам изменения БД
     * @param status
     */
    public void setEnabled(boolean status){
            binding.btnAdd.setEnabled(status);
            binding.btnEdit.setEnabled(status);
            binding.btnDelete.setEnabled(status);
        if (categoryID < 0){
            binding.btnEdit.setEnabled(false);
            binding.btnDelete.setEnabled(false);
        }
    }

    /**
     * Метод считывания данных из БД
     */
    public void readDB(){
        Log.d(tagDB, "Вызов метода readDB фрагмента CategoryFragment");
        database = dbHelper.getReadableDatabase();
        cursor = database.query(DBHelper.TABLE_NAME_CATEGORIES,
                null,
                null,
                null,
                null,
                null,
                null);
        simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_category , cursor, from, to, 0);
        binding.lvCategories.setAdapter(simpleCursorAdapter);
        binding.lvCategories.setOnItemClickListener(this::onClickList);


        if(cursor.moveToFirst() == true){
            //Получение индексов
            do {
                simpleCursorAdapter.changeCursor(cursor);
                binding.lvCategories.setOnItemClickListener(this::onClickList);
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
    public void onClickList(AdapterView<?> parent, View view, int position, long id) {
        categoryID = (int) id;
        if (getArguments().getString("action").equals("choose")) {
            NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            String key = "select_category";
            Bundle bundle = new Bundle();
            bundle.putInt(key, categoryID);
            requireActivity().getSupportFragmentManager().setFragmentResult(key, bundle);
            host.popBackStack();
        } else {
            if (rowIsExist(DBHelper.TABLE_NAME_CATEGORIES, DBHelper.KEY_CATEGORIES_ID, categoryID)) {
                Cursor item = (Cursor) simpleCursorAdapter.getItem(position);
                binding.etName.setText(item.getString(item.getColumnIndexOrThrow(from[0])));
                binding.etAddedValue.setText(item.getString(item.getColumnIndexOrThrow(from[1])));
                binding.etDiscount.setText(item.getString(item.getColumnIndexOrThrow(from[2])));
                setEnabled(true);
            } else {
                setEnabled(false);
            }
        }
    }

    /**
     * Проверка наличия выбранной записи данных в БД
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
     * Метод для проверки введеных данных пользователем
     * @return
     */
    public boolean isCorrectInput(){
        if(!Validation.isRightName(binding.etName.getText().toString())) return false;
        if(!Validation.isFloat(binding.etAddedValue.getText().toString())) return false;
        if(!Validation.isFloat(binding.etDiscount.getText().toString())) return false;
        return true;
    }

    /**
     * Метод добавления записи в БД
     * @param view
     */
    public void onAdd(View view){
        Log.d(tagDB, "Вызов метода onAdd фрагмента CategoryFragment");
        database = dbHelper.getWritableDatabase();
        if(!isCorrectInput()) {
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct), Toast.LENGTH_LONG);
            Log.d(tagDB, getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct));
        }
        else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_CATEGORIES_NAME, binding.etName.getText().toString());
            contentValues.put(DBHelper.KEY_CATEGORIES_ADDED_VALUE, binding.etAddedValue.getText().toString());
            contentValues.put(DBHelper.KEY_CATEGORIES_DISCOUNT, binding.etDiscount.getText().toString());

            long result = database.insert(DBHelper.TABLE_NAME_CATEGORIES, null, contentValues);

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
     * Метод изменения выбранной записи данных в БД
     * @param view
     */
    public void onEdit(View view){
        Log.d(tagDB, "Вызов метода onEdit фрагмента CategoryFragment");
        if(!isCorrectInput()) {
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct), Toast.LENGTH_LONG);
            Log.d(tagDB, getResources().getString(R.string.action_result_NOT_OK)
                    + " - " + getResources().getString(R.string.action_result_input_not_correct));
        }
        else {
            database = dbHelper.getWritableDatabase();
            if (rowIsExist(DBHelper.TABLE_NAME_DIRECTIONS, DBHelper.KEY_DIRECTIONS_ID, categoryID)) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_CATEGORIES_NAME, binding.etName.getText().toString());
                contentValues.put(DBHelper.KEY_CATEGORIES_ADDED_VALUE, binding.etAddedValue.getText().toString());
                contentValues.put(DBHelper.KEY_CATEGORIES_DISCOUNT, binding.etDiscount.getText().toString());
                String where = DBHelper.KEY_CATEGORIES_ID + "=" + categoryID;

                long result = database.update(DBHelper.TABLE_NAME_CATEGORIES, contentValues, where, null);
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
     * Метод удаления выбранной записи из БД
     * @param view
     */
    public void onDelete(View view){
        Log.d(tagDB, "Вызов метода onDelete фрагмента CategoryFragment");
        database = dbHelper.getWritableDatabase();

        if (rowIsExist(DBHelper.TABLE_NAME_CATEGORIES,DBHelper.KEY_CATEGORIES_ID,categoryID)) {
            if (!rowIsExist(DBHelper.TABLE_NAME_TOURS, DBHelper.KEY_TOURS_ID_CATEGORY, categoryID)) {
                long result = database.delete(dbHelper.TABLE_NAME_CATEGORIES,
                        DBHelper.KEY_CATEGORIES_ID + " = ?", new String[]{String.valueOf(categoryID)});
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