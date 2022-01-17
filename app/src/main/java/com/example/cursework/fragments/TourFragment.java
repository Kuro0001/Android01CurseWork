package com.example.cursework.fragments;

import android.app.backup.BackupHelper;
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
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.cursework.R;
import com.example.cursework.dataBase.DBHelper;
import com.example.cursework.databinding.FragmentTourBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TourFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TourFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    Toast toast;
    public static final String tagDB = "tagFragment";
    private FragmentTourBinding binding;
    DBHelper dbHelper;
    SQLiteDatabase database;
    SimpleCursorAdapter simpleCursorAdapter;
    Cursor cursor;
    int selectedID;
    String[] months = new String[12];
    String[] years = new String[50];
    int month = 0;
    int year = 0;

    public static String[] names = new String[] {
            DBHelper.KEY_TOURS_ID,
            "tourName",
            "tourDaysCount",
            "tourPrice",
            "categoryName"
    };

    public static String[] from = new String[] {
            names[1],
            names[2],
            names[3],
            names[4]
    };
    public static int[] to = new int[]{
            R.id.etName,
            R.id.etDaysCount,
            R.id.etPrice,
            R.id.etCategory
    };

    public TourFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TourFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TourFragment newInstance(String param1, String param2) {
        TourFragment fragment = new TourFragment();
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
        binding = FragmentTourBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        binding.btnSearch.setOnClickListener(this::onSearch);
        binding.btnAdd.setOnClickListener(this::onAdd);
        binding.lvTour.setOnItemClickListener(this::onClickList);
        dbHelper = new DBHelper(getContext());
        setSpinners();
        readDB();
        return root;
//        return inflater.inflate(R.layout.fragment_tour, container, false);
    }

    /**
     * Метод для очистки фрагмента из памяти
     */
    @Override
    public void onDestroyView() {
        Log.d(tagDB, "Вызов метода onDestroyView фрагмента TourFragment");
        super.onDestroyView();
        binding = null;
//        cursor.close();
    }

    /**
     * Метод заполнения спиннеров даты для их далнейшей эксплуатации
     */
    public void setSpinners(){
        Log.d(tagDB, "Вызов метода setSpinners фрагмента TourFragment");
        for (int i=1;i<10;i++){
            months[i-1] = 0 + String.valueOf(i);
        }
        for (int i=10;i<13;i++){
            months[i-1] = String.valueOf(i);
        }
        for (int i=0;i<50;i++) {
            years[i] = String.valueOf(2000 + i);
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, months);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnMonth.setAdapter(adapter1);
        binding.spnMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, years);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnYear.setAdapter(adapter2);
        binding.spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * Метод считывания данных из БД
     */
    public void readDB(){
        Log.d(tagDB, "Вызов метода readDB фрагмента TourFragment");
        database = dbHelper.getReadableDatabase();
        String query = "SELECT " + DBHelper.TABLE_NAME_TOURS + "." + DBHelper.KEY_TOURS_ID  +
                ", " + DBHelper.TABLE_NAME_TOURS + "." + DBHelper.KEY_TOURS_NAME + " as " + names[1] +
                ", " + DBHelper.TABLE_NAME_TOURS + "." + DBHelper.KEY_TOURS_DAYS_COUNT + " as " + names[2] +
                ", " + DBHelper.TABLE_NAME_TOURS + "." + DBHelper.KEY_TOURS_PRICE + " as " + names[3] +
                ", " + DBHelper.TABLE_NAME_CATEGORIES + "." + DBHelper.KEY_CATEGORIES_NAME + " as " + names[4] +
                " FROM " + DBHelper.TABLE_NAME_TOURS  +
                " INNER JOIN " + DBHelper.TABLE_NAME_CATEGORIES +
                " on " + DBHelper.TABLE_NAME_TOURS + "." + DBHelper.KEY_TOURS_ID_CATEGORY  +
                " = " + DBHelper.TABLE_NAME_CATEGORIES + "." + DBHelper.KEY_CATEGORIES_ID;

        cursor = database.rawQuery(query,null);
        simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_hotel , cursor, from, to, 0);
        binding.lvTour.setAdapter(simpleCursorAdapter);
        binding.lvTour.setOnItemClickListener(this::onClickList);


        if(cursor.moveToFirst() == true){
            do {
                simpleCursorAdapter.changeCursor(cursor);
                binding.lvTour.setOnItemClickListener(this::onClickList);
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
        host.navigate(R.id.fragment_tour_detail,bundle);
    }

    /**
     * Метод для создания новой записи
     * @param view
     */
    public void onAdd(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("id", -1);
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_tour_detail,bundle);
    }
    /**
     * Метод для поиска клиента по указанному паспорту
     * @param view
     */
    public void onSearch(View view){
        String date1 = years[year] + months[month] + "00";
        String date2;
        if (month == 11)
            date2 = years[year+1] + months[month] + "00";
        else
            if(year != years.length)
            date2 = years[year] + months[month+1] + "00";
            else
                date2 = years[years.length] + months[months.length] + "99";
        String directionName = binding.etDirectionName.getText().toString();

        database = dbHelper.getReadableDatabase();
        String query = "SELECT " + DBHelper.TABLE_NAME_TOURS + "." + DBHelper.KEY_TOURS_ID  +
                ", " + DBHelper.TABLE_NAME_TOURS + "." + DBHelper.KEY_TOURS_NAME + " as " + names[1] +
                ", " + DBHelper.TABLE_NAME_TOURS + "." + DBHelper.KEY_TOURS_DAYS_COUNT + " as " + names[2] +
                ", " + DBHelper.TABLE_NAME_TOURS + "." + DBHelper.KEY_TOURS_PRICE + " as " + names[3] +
                ", " + DBHelper.TABLE_NAME_CATEGORIES + "." + DBHelper.KEY_CATEGORIES_NAME + " as " + names[4] +
                " FROM " + DBHelper.TABLE_NAME_TOURS  +
                " INNER JOIN " + DBHelper.TABLE_NAME_CATEGORIES +
                " on " + DBHelper.TABLE_NAME_TOURS + "." + DBHelper.KEY_TOURS_ID_CATEGORY  +
                " = " + DBHelper.TABLE_NAME_CATEGORIES + "." + DBHelper.KEY_CATEGORIES_ID +
                " WHERE " + DBHelper.TABLE_NAME_TOURS + "." + DBHelper.KEY_TOURS_START_DATE +
                " > " + date1 + " AND "+ DBHelper.TABLE_NAME_TOURS + "." + DBHelper.KEY_TOURS_START_DATE +
                " < " + date2 + " AND " + DBHelper.TABLE_NAME_HOTELS + "." + DBHelper.KEY_HOTELS_ID +
                " IN (SELECT " + DBHelper.TABLE_NAME_HOTELS + "." + DBHelper.KEY_HOTELS_ID +
                " INNER JOIN " + DBHelper.TABLE_NAME_DIRECTIONS + " as direction" +
                " on " + DBHelper.TABLE_NAME_HOTELS + "." + DBHelper.KEY_HOTELS_ID_DIRECTION +
                " = " + DBHelper.TABLE_NAME_DIRECTIONS + "." + DBHelper.KEY_DIRECTIONS_ID +
                " WHERE " + DBHelper.TABLE_NAME_DIRECTIONS + "." + DBHelper.KEY_DIRECTIONS_NAME +
                " LIKE '%" + directionName + "%'" + ")";

        cursor = database.rawQuery(query,null);


        if(cursor.moveToFirst() == true) {
            {
                simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_client , cursor, from, to, 0);
                binding.lvTour.setAdapter(simpleCursorAdapter);
                binding.lvTour.setOnItemClickListener(this::onClickList);
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_search_OK,cursor.getCount()),Toast.LENGTH_LONG);
                Log.d(tagDB, getResources().getString(R.string.action_search_OK,cursor.getCount()));
                if(cursor.moveToFirst() == true){
                    //Получение индексов
                    do {
                        simpleCursorAdapter.changeCursor(cursor);
                        binding.lvTour.setOnItemClickListener(this::onClickList);
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