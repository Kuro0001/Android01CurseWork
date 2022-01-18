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

import com.example.cursework.R;
import com.example.cursework.dataBase.DBHelper;
import com.example.cursework.databinding.FragmentTourBinding;
import com.example.cursework.databinding.FragmentVoucherBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VoucherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VoucherFragment extends Fragment {

    Toast toast;
    public static final String tagDB = "tagFragment";
    private FragmentVoucherBinding binding;
    DBHelper dbHelper;
    SQLiteDatabase database;
    SimpleCursorAdapter simpleCursorAdapter;
    Cursor cursor;
    int selectedID;

    public static String[] names = new String[] {
            DBHelper.KEY_TOURS_ID,
            "voucherName",
            "voucherPrice",
            "tourName"
    };

    public static String[] from = new String[] {
            names[1],
            names[2],
            names[3]
    };
    public static int[] to = new int[]{
            R.id.etName,
            R.id.etPrice,
            R.id.etTourName
    };


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VoucherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VoucherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VoucherFragment newInstance(String param1, String param2) {
        VoucherFragment fragment = new VoucherFragment();
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
        binding = FragmentVoucherBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        binding.btnSearch.setOnClickListener(this::onSearch);
        binding.btnAdd.setOnClickListener(this::onAdd);
        binding.lvVouchers.setOnItemClickListener(this::onClickList);
        dbHelper = new DBHelper(getContext());
        readDB();
        return root;
//        return inflater.inflate(R.layout.fragment_voucher, container, false);
    }

    /**
     * Метод для очистки фрагмента из памяти
     */
    @Override
    public void onDestroyView() {
        Log.d(tagDB, "Вызов метода onDestroyView фрагмента VoucherFragment");
        super.onDestroyView();
        binding = null;
//        cursor.close();
    }

    /**
     * Метод считывания данных из БД
     */
    public void readDB(){
        Log.d(tagDB, "Вызов метода readDB фрагмента VoucherFragment");
        database = dbHelper.getReadableDatabase();
        String query = "SELECT " + DBHelper.TABLE_NAME_VOUCHERS + "." + DBHelper.KEY_VOUCHERS_ID  +
                ", " + DBHelper.TABLE_NAME_VOUCHERS + "." + DBHelper.KEY_VOUCHERS_NAME + " as " + names[1] +
                ", " + DBHelper.TABLE_NAME_VOUCHERS + "." + DBHelper.KEY_VOUCHERS_PRICE + " as " + names[2] +
                ", " + DBHelper.TABLE_NAME_TOURS + "." + DBHelper.KEY_TOURS_NAME + " as " + names[3] +
                " FROM " + DBHelper.TABLE_NAME_VOUCHERS +
                " INNER JOIN " + DBHelper.TABLE_NAME_TOURS +
                " on " + DBHelper.TABLE_NAME_VOUCHERS + "." + DBHelper.KEY_VOUCHERS_ID_TOUR  +
                " = " + DBHelper.TABLE_NAME_TOURS + "." + DBHelper.KEY_TOURS_ID;

        cursor = database.rawQuery(query,null);
        simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_voucher , cursor, from, to, 0);
        binding.lvVouchers.setAdapter(simpleCursorAdapter);
        binding.lvVouchers.setOnItemClickListener(this::onClickList);


        if(cursor.moveToFirst() == true){
            do {
                simpleCursorAdapter.changeCursor(cursor);
                binding.lvVouchers.setOnItemClickListener(this::onClickList);
                cursor.moveToNext();
            } while (cursor.isAfterLast() != true);
        }
//        cursor.close();
    }

    /**
     * Метод взаимдействия пользователя со списком данных (событие нажатия на элемент списка)
     * перенаправление на детальную информацию записи
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    public void onClickList(AdapterView<?> parent, View view, int position, long id){
        Bundle bundle = new Bundle();
        bundle.putInt("id", (int)id);
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.navigation_new_voucher,bundle);
    }
    /**
     * Метод для создания новой записи
     * @param view
     */
    public void onAdd(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("id", -1);
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.navigation_new_voucher,bundle);
    }
    /**
     * Метод для поиска путевок по указанному паспорту клиента
     * @param view
     */
    public void onSearch(View view){
        Log.d(tagDB, "Вызов метода onSearch фрагмента VoucherFragment");
        database = dbHelper.getReadableDatabase();
        String query = "SELECT " + DBHelper.TABLE_NAME_VOUCHERS + "." + DBHelper.KEY_VOUCHERS_ID  +
                ", " + DBHelper.TABLE_NAME_VOUCHERS + "." + DBHelper.KEY_VOUCHERS_NAME + " as " + names[1] +
                ", " + DBHelper.TABLE_NAME_VOUCHERS + "." + DBHelper.KEY_VOUCHERS_PRICE + " as " + names[2] +
                ", " + DBHelper.TABLE_NAME_TOURS + "." + DBHelper.KEY_TOURS_NAME + " as " + names[3] +
                " FROM " + DBHelper.TABLE_NAME_VOUCHERS  +
                " INNER JOIN " + DBHelper.TABLE_NAME_TOURS +
                " on " + DBHelper.TABLE_NAME_VOUCHERS + "." + DBHelper.KEY_VOUCHERS_ID_TOUR  +
                " = " + DBHelper.TABLE_NAME_TOURS + "." + DBHelper.KEY_TOURS_ID +
                " WHERE " + DBHelper.TABLE_NAME_VOUCHERS + "." + DBHelper.KEY_VOUCHERS_ID_CLIENT +
                " IN (SELECT " + DBHelper.TABLE_NAME_CLIENTS + "." + DBHelper.KEY_CLIENTS_ID +
                " FROM " + DBHelper.TABLE_NAME_CLIENTS +
                " WHERE " + DBHelper.TABLE_NAME_CLIENTS + "." + DBHelper.KEY_CLIENTS_PASSPORT +
                " LIKE '%" + binding.etClientPassport.getText().toString() + "%'";
        cursor = database.rawQuery(query,null);

        if(cursor.moveToFirst() == true) {
            {
                simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_voucher , cursor, from, to, 0);
                binding.lvVouchers.setAdapter(simpleCursorAdapter);
                binding.lvVouchers.setOnItemClickListener(this::onClickList);
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_search_OK,cursor.getCount()),Toast.LENGTH_LONG);
                Log.d(tagDB, getResources().getString(R.string.action_search_OK,cursor.getCount()));
                if(cursor.moveToFirst() == true){
                    //Получение индексов
                    do {
                        simpleCursorAdapter.changeCursor(cursor);
                        binding.lvVouchers.setOnItemClickListener(this::onClickList);
                        cursor.moveToNext();
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