package com.example.cursework.fragments.detailed;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.cursework.Authorisation;
import com.example.cursework.R;
import com.example.cursework.dataBase.DBHelper;
import com.example.cursework.databinding.FragmentClientDetailBinding;
import com.example.cursework.databinding.FragmentTourDetailBinding;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TourDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TourDetailFragment extends Fragment {

    Toast toast;
    public static final String tagDB = "tagFragment";
    private FragmentTourDetailBinding binding;
    DBHelper dbHelper;
    SQLiteDatabase database;
    Cursor cursor;
    int selectedID;
    int selectedHotelID;
    int selectedTourOperatorID;
    int selectedKindID;
    int selectedCategoryID;
    int vouchersCount;
    String[] days = new String[31];
    String[] months = new String[12];
    String[] years = new String[50];
    int day = 0;
    int month = 0;
    int year = 0;
    String key1 = "select_kind";
    String key2 = "select_category";
    String key3 = "select_tour_operator";
    String key4 = "select_hotel";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TourDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TourDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TourDetailFragment newInstance(String param1, String param2) {
        TourDetailFragment fragment = new TourDetailFragment();
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
        binding = FragmentTourDetailBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        binding.btnAdd.setOnClickListener(this::onAdd);
        binding.btnEdit.setOnClickListener(this::onEdit);
        binding.btnDelete.setOnClickListener(this::onDelete);
        binding.btnOnKind.setOnClickListener(this::onKind);
        binding.btnOnCategory.setOnClickListener(this::onCategory);
        binding.btnOnTourOperator.setOnClickListener(this::onTourOperator);
        binding.btnOnHotel.setOnClickListener(this::onHotel);
        dbHelper = new DBHelper(getContext());
        selectedID = getArguments().getInt("id");
        if (selectedID > 0) {
            setEnabled(true);
            readDb();
        } else {
            setEnabled(false);
        }
        setSpinners();
        setListeners();

        return root;
//        return inflater.inflate(R.layout.fragment_tour_detail, container, false);
    }

    /**
     * Метод для установки слушателей при нажатиях кнопок для перехода на другие таблицы
     */
    public void setListeners(){
        requireActivity().getSupportFragmentManager().setFragmentResultListener(key1, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey == key1){
                    selectedKindID = result.getInt(requestKey);
                    setKind();
                }
            }
        });
        requireActivity().getSupportFragmentManager().setFragmentResultListener(key2, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey == key2){
                    selectedCategoryID = result.getInt(requestKey);
                    setCategory();
                }
            }
        });
        requireActivity().getSupportFragmentManager().setFragmentResultListener(key3, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey == key3){
                    selectedTourOperatorID = result.getInt(requestKey);
                    setTourOperator();
                }
            }
        });
        requireActivity().getSupportFragmentManager().setFragmentResultListener(key4, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey == key4){
                    selectedHotelID = result.getInt(requestKey);
                    setHotel();
                }
            }
        });
    }

    /**
     * Метод заполнения спиннеров даты для их далнейшей эксплуатации
     */
    public void setSpinners(){
        Log.d(tagDB, "Вызов метода setSpinners фрагмента TourDetailFragment");
        for (int i=1;i<10;i++){
            days[i-1] = 0 + String.valueOf(i);
        }
        for (int i=10;i<32;i++){
            days[i-1] = String.valueOf(i);
        }
        for (int i=1;i<10;i++){
            months[i-1] = 0 + String.valueOf(i);
        }
        for (int i=10;i<13;i++){
            months[i-1] = String.valueOf(i);
        }
        for (int i=0;i<50;i++) {
            years[i] = String.valueOf(2000 + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnDay.setAdapter(adapter);
        binding.spnDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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

        if(getArguments().getInt("id") > 0){
            binding.spnMonth.setSelection(day);
            binding.spnDay.setSelection(month);
            binding.spnYear.setSelection(year);
        }
    }

    /**
     * Метод предоставления и закрытия доступа к компонентам изменения БД
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
     * Метод считывания записи Клиента из БД
     */
    public void readDb() {
        Log.d(tagDB, "Вызов метода readDb фрагмента TourDetailFragment");
        database = dbHelper.getReadableDatabase();
        cursor = database.query(DBHelper.TABLE_NAME_TOURS,
                null,
                DBHelper.KEY_CLIENTS_ID + " = ?",
                new String[]{
                        String.valueOf(selectedID)},
                null,
                null,
                null);

        if (cursor.moveToFirst() == true) {
            int indID = cursor.getColumnIndex(DBHelper.KEY_TOURS_ID);
            selectedID = cursor.getInt(indID);
            int indName = cursor.getColumnIndex(DBHelper.KEY_TOURS_NAME);
            binding.etName.setText(cursor.getString(indName));
            int indDaysCount = cursor.getColumnIndex(DBHelper.KEY_TOURS_DAYS_COUNT);
            binding.etDaysCount.setText(cursor.getInt(indDaysCount));
            int indOffers = cursor.getColumnIndex(DBHelper.KEY_TOURS_OFFERS_ALL);
            binding.etOffersCount.setText(String.valueOf(cursor.getFloat(indOffers)));
            int indPrice = cursor.getColumnIndex(DBHelper.KEY_TOURS_PRICE);
            binding.etPrice.setText(String.valueOf(cursor.getFloat(indPrice)));

            String query = "SELECT COUNT (*) as vouchers_count FROM " + DBHelper.TABLE_NAME_VOUCHERS +
                    " WHERE " + DBHelper.TABLE_NAME_VOUCHERS + "." + DBHelper.KEY_VOUCHERS_ID_TOUR +
                    " = " + selectedID;
            Cursor cursor1 = database.rawQuery(query, null);
            int indVouchersCount = cursor1.getColumnIndex("vouchers_count");
            binding.etVouchers.setText(String.valueOf(cursor1.getInt(indVouchersCount)));

            int indDate = cursor.getColumnIndex(DBHelper.KEY_TOURS_START_DATE);
            long startDate = cursor.getLong(indDate);
            day = (int)(startDate % 100)-1;
            month = (int)(startDate /100 % 100)-1;
            year = (int)(2050 - startDate /10000);
            setSpinners();

            int indTourOperator = cursor.getColumnIndex(DBHelper.KEY_TOURS_ID_TOUR_OPERATOR);
            selectedTourOperatorID = cursor.getInt(indTourOperator);
            setTourOperator();
            int indKind = cursor.getColumnIndex(DBHelper.KEY_TOURS_ID_KIND);
            selectedKindID = cursor.getInt(indKind);
            setKind();
            int indCategory = cursor.getColumnIndex(DBHelper.KEY_TOURS_ID_CATEGORY);
            selectedCategoryID = cursor.getInt(indCategory);
            setCategory();
            int indHotel = cursor.getColumnIndex(DBHelper.KEY_TOURS_ID_HOTEL);
            selectedHotelID = cursor.getInt(indHotel);
            setHotel();
        }
//        cursor.close();
    }

    /**
     * Метод заполненния полей туроператора на форме туров
     */
    public void setTourOperator(){
        database = dbHelper.getReadableDatabase();
        Cursor cursor1 = database.query(DBHelper.TABLE_NAME_TOUR_OPERATORS,
                null,
                DBHelper.KEY_TOUR_OPERATORS_ID + " = ?",
                new String[]{
                        String.valueOf(selectedTourOperatorID)},
                null,
                null,
                null);
        if (cursor1.moveToFirst() == true) {
            int indName = cursor1.getColumnIndex(DBHelper.KEY_TOUR_OPERATORS_NAME);
            binding.etTourOperatorName.setText(cursor1.getString(indName));
        }
    }
    /**
     * Метод заполненния полей вида тура на форме туров
     */
    public void setKind(){
        database = dbHelper.getReadableDatabase();
        Cursor cursor1 = database.query(DBHelper.TABLE_NAME_KINDS,
                null,
                DBHelper.KEY_KINDS_ID + " = ?",
                new String[]{
                        String.valueOf(selectedKindID)},
                null,
                null,
                null);
        if (cursor1.moveToFirst() == true) {
            int indName = cursor1.getColumnIndex(DBHelper.KEY_KINDS_NAME);
            binding.etKindName.setText(cursor1.getString(indName));
        }
    }
    /**
     * Метод заполненния полей категории тура на форме туров
     */
    public void setCategory(){
        database = dbHelper.getReadableDatabase();
        Cursor cursor1 = database.query(DBHelper.TABLE_NAME_CATEGORIES,
                null,
                DBHelper.KEY_CATEGORIES_ID + " = ?",
                new String[]{
                        String.valueOf(selectedCategoryID)},
                null,
                null,
                null);
        if (cursor1.moveToFirst() == true) {
            int indName = cursor1.getColumnIndex(DBHelper.KEY_CATEGORIES_NAME);
            binding.etCategoryName.setText(cursor1.getString(indName));
            int indDiscount = cursor1.getColumnIndex(DBHelper.KEY_CATEGORIES_DISCOUNT);
            binding.etCategoryDiscount.setText(cursor1.getString(indDiscount));
            int indAddedValue = cursor1.getColumnIndex(DBHelper.KEY_CATEGORIES_ADDED_VALUE);
            binding.etCategoryAddedValue.setText(cursor1.getString(indAddedValue));
        }
    }
    /**
     * Метод заполненния полей отеля на форме туров
     */
    public void setHotel(){
        database = dbHelper.getReadableDatabase();
        Cursor cursor1;
        String query = "SELECT hotel." + DBHelper.KEY_HOTELS_ID +
                ", hotel." + DBHelper.KEY_HOTELS_NAME + " as hotelName" +
                ", direction." + DBHelper.KEY_DIRECTIONS_NAME + " as directionName" +
                " FROM " + DBHelper.TABLE_NAME_HOTELS + " as hotel " +
                " INNER JOIN " + DBHelper.TABLE_NAME_DIRECTIONS + " as direction" +
                " on hotel." + DBHelper.KEY_HOTELS_ID_DIRECTION +
                " = direction." + DBHelper.KEY_DIRECTIONS_ID +
                " WHERE hotel." + DBHelper.KEY_HOTELS_ID +
                " = " + selectedHotelID;
        cursor1 = database.rawQuery(query, null);
        if (cursor1.moveToFirst() == true) {
            int indHotelName = cursor1.getColumnIndex("hotelName");
            binding.etHotelName.setText(cursor1.getString(indHotelName));
            int indDirectionName = cursor1.getColumnIndex("directionName");
            binding.etHotelDirectionName.setText(cursor1.getString(indDirectionName));
        }
    }

    /**
     * Проверка наличия выбранной записи данных в БД
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
     * Метод нажатия на кнопку перехода на фрагмент видов туров для выбора
     */
    public void onKind(View view){
        Bundle bundle = new Bundle();
        bundle.putString("action", "choose");
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_kinds, bundle);
    }
    /**
     * Метод нажатия на кнопку перехода на фрагмент категорий туров для выбора
     */
    public void onCategory(View view){
        Bundle bundle = new Bundle();
        bundle.putString("action", "choose");
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_category, bundle);
    }
    /**
     * Метод нажатия на кнопку перехода на фрагмент туроператоров для выбора
     */
    public void onTourOperator(View view){
        Bundle bundle = new Bundle();
        bundle.putString("action", "choose");
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_tour_operator, bundle);
    }
    /**
     * Метод нажатия на кнопку перехода на фрагмент отелей для выбора
     */
    public void onHotel(View view){
        Bundle bundle = new Bundle();
        bundle.putString("action", "choose");
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_hotel, bundle);
    }



    /**
     * Метод добавления записи в БД
     * @param view
     */
    public void onAdd(View view) {
        Log.d(tagDB, "Вызов метода onAdd фрагмента TourDetailFragment");
        database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_TOURS_NAME, binding.etName.getText().toString());
        contentValues.put(DBHelper.KEY_TOURS_DAYS_COUNT, binding.etDaysCount.getText().toString());
        contentValues.put(DBHelper.KEY_TOURS_PRICE, binding.etPrice.getText().toString());
        contentValues.put(DBHelper.KEY_TOURS_OFFERS_ALL, binding.etOffersCount.getText().toString());
        String date = years[year] + months[month] + days[day];
        contentValues.put(DBHelper.KEY_TOURS_START_DATE, date);
        contentValues.put(DBHelper.KEY_TOURS_ID_KIND, String.valueOf(selectedKindID));
        contentValues.put(DBHelper.KEY_TOURS_ID_CATEGORY, String.valueOf(selectedCategoryID));
        contentValues.put(DBHelper.KEY_TOURS_ID_TOUR_OPERATOR, String.valueOf(selectedTourOperatorID));
        contentValues.put(DBHelper.KEY_TOURS_ID_HOTEL, String.valueOf(selectedHotelID));

        long result = database.insert(DBHelper.TABLE_NAME_TOURS, null, contentValues);
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
    public void onEdit(View view) {
        Log.d(tagDB, "Вызов метода onEdit фрагмента TourDetailFragment");
        database = dbHelper.getWritableDatabase();
        if (rowIsExist(DBHelper.TABLE_NAME_TOURS, DBHelper.KEY_TOURS_ID, selectedID)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_TOURS_NAME, binding.etName.getText().toString());
            contentValues.put(DBHelper.KEY_TOURS_DAYS_COUNT, binding.etDaysCount.getText().toString());
            contentValues.put(DBHelper.KEY_TOURS_PRICE, binding.etPrice.getText().toString());
            contentValues.put(DBHelper.KEY_TOURS_OFFERS_ALL, binding.etOffersCount.getText().toString());
            String date = years[year] + months[month] + days[day];
            contentValues.put(DBHelper.KEY_TOURS_START_DATE, date);
            contentValues.put(DBHelper.KEY_TOURS_ID_KIND, String.valueOf(selectedKindID));
            contentValues.put(DBHelper.KEY_TOURS_ID_CATEGORY, String.valueOf(selectedCategoryID));
            contentValues.put(DBHelper.KEY_TOURS_ID_TOUR_OPERATOR, String.valueOf(selectedTourOperatorID));
            contentValues.put(DBHelper.KEY_TOURS_ID_HOTEL, String.valueOf(selectedHotelID));
            String where = DBHelper.KEY_TOURS_ID + "=" + selectedID;

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
        toast.show();
    }

    /**
     * Метод удаления выбранной записи из БД
     * @param view
     */
    public void onDelete(View view) {
        Log.d(tagDB, "Вызов метода onDelete фрагмента TourDetailFragment");
        database = dbHelper.getWritableDatabase();

        if (rowIsExist(DBHelper.TABLE_NAME_TOURS, DBHelper.KEY_TOURS_ID, selectedID)) {
            if (!rowIsExist(DBHelper.TABLE_NAME_VOUCHERS, DBHelper.KEY_VOUCHERS_ID_TOUR, selectedID)) {
                long result = database.delete(dbHelper.TABLE_NAME_TOURS,
                        DBHelper.KEY_TOURS_ID + " = ?", new String[]{String.valueOf(selectedID)});
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