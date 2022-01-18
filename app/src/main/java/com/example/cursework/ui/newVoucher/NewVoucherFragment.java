package com.example.cursework.ui.newVoucher;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.cursework.Authorisation;
import com.example.cursework.R;
import com.example.cursework.dataBase.DBHelper;
import com.example.cursework.databinding.FragmentNewVoucherBinding;
import com.example.cursework.databinding.FragmentTourDetailBinding;

import java.util.Calendar;

public class NewVoucherFragment extends Fragment {

    Toast toast;
    public static final String tagDB = "tagFragment";
    private FragmentNewVoucherBinding binding;
    Bundle arguments;
    DBHelper dbHelper;
    SQLiteDatabase database;
    Cursor cursor;
    int selectedID = -1;
    int selectedClientID;
    int selectedTourID;
    int selectedClientID1 = -1;
    int selectedTourID1 = -1;
    String key1 = "select_tour";
    String key2 = "select_client";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewVoucherBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.btnAdd.setOnClickListener(this::onAdd);
        binding.btnEdit.setOnClickListener(this::onEdit);
        binding.btnDelete.setOnClickListener(this::onDelete);
        binding.btnOnTour.setOnClickListener(this::onTour);
        binding.btnOnClient.setOnClickListener(this::onClient);
        dbHelper = new DBHelper(getContext());
        arguments = getArguments();
        if (arguments != null && arguments.containsKey("id"))
            selectedID = getArguments().getInt("id");
        if (selectedID > 0) {
            readDb();
        }
        if (Authorisation.isLoggedIn) setEnabled(true);
        else setEnabled(false);
        setListeners();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Метод для установки слушателей при нажатиях кнопок для перехода на другие таблицы
     */
    public void setListeners(){
        requireActivity().getSupportFragmentManager().setFragmentResultListener(key1, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals(key1)){
                    Log.d(tagDB, "Вызов метода setListeners фрагмента NewVoucherFragment - key 1");
                    selectedTourID1 = result.getInt(requestKey);
                    setTour();
                    setVoucher();
                    setVoucherName();
                }
            }
        });
        requireActivity().getSupportFragmentManager().setFragmentResultListener(key2, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals(key2)){
                    Log.d(tagDB, "Вызов метода setListeners фрагмента NewVoucherFragment - key 2");
                    selectedClientID1 = result.getInt(requestKey);
                    setClient();
                    setVoucherName();
                }
            }
        });
    }

    /**
     * Метод предоставления и закрытия доступа к компонентам изменения БД
     * @param status
     */
    public void setEnabled(boolean status) {
        binding.btnAdd.setEnabled(status);
        binding.btnEdit.setEnabled(status);
        binding.btnDelete.setEnabled(status);

        if (selectedID < 0){
            binding.btnEdit.setEnabled(false);
            binding.btnDelete.setEnabled(false);
        }
    }

    /**
     * Метод считывания записи Клиента из БД
     */
    public void readDb() {
        Log.d(tagDB, "Вызов метода readDb фрагмента NewVoucherFragment");
        database = dbHelper.getReadableDatabase();
        cursor = database.query(DBHelper.TABLE_NAME_VOUCHERS,
                null,
                DBHelper.KEY_VOUCHERS_ID + " = ?",
                new String[]{
                        String.valueOf(selectedID)},
                null,
                null,
                null);

        if (cursor.moveToFirst() == true) {
            int indID = cursor.getColumnIndex(DBHelper.KEY_VOUCHERS_ID);
            selectedID = cursor.getInt(indID);
            int indName = cursor.getColumnIndex(DBHelper.KEY_VOUCHERS_NAME);
            binding.etVoucherName.setText(cursor.getString(indName));
            int indDate = cursor.getColumnIndex(DBHelper.KEY_VOUCHERS_DATE);
            long startDate = cursor.getInt(indDate);
            String day = String.valueOf(startDate % 100);
            String month = String.valueOf(startDate /100 % 100);
            String year = String.valueOf(startDate /10000);
            binding.etVoucherDate.setText(year + "." + month + "." + day);
            int indPrice = cursor.getColumnIndex(DBHelper.KEY_VOUCHERS_PRICE);
            binding.etVoucherPrice.setText(String.valueOf(cursor.getFloat(indPrice)));


            int indTour = cursor.getColumnIndex(DBHelper.KEY_VOUCHERS_ID_TOUR);
            selectedTourID = cursor.getInt(indTour);
            setTour();
            int indСlient = cursor.getColumnIndex(DBHelper.KEY_VOUCHERS_ID_CLIENT);
            selectedClientID = cursor.getInt(indСlient);
            setClient();
            int indEmployee = cursor.getColumnIndex(DBHelper.KEY_VOUCHERS_ID_EMPLOYEE);
            selectedClientID = cursor.getInt(indEmployee);
            setEmployee(selectedClientID);
        }
//        cursor.close();
    }

    /**
     * Метод заполненния полей тура на форме путевок
     */
    public void setTour(){
        Log.d(tagDB, "Вызов метода setTour фрагмента NewVoucherFragment");
        if (selectedTourID1 > -1)
            selectedTourID = selectedTourID1;
        database = dbHelper.getReadableDatabase();
        Cursor cursor1;
        String[] names = new String[]{
                DBHelper.KEY_TOURS_ID,
                "tourName",
                "tourPrice",
                "tourStartDate",
                "tourDaysCount",
                "directionName",
                "categoryName",
                "categoryDiscount",
                "categoryAddedValue"
        };
        String query = "SELECT tour." + DBHelper.KEY_TOURS_ID +
                ", tour." + DBHelper.KEY_TOURS_NAME + " as " + names[1] +
                ", tour." + DBHelper.KEY_TOURS_PRICE + " as " + names[2] +
                ", tour." + DBHelper.KEY_TOURS_START_DATE + " as " + names[3] +
                ", tour." + DBHelper.KEY_TOURS_DAYS_COUNT + " as " + names[4] +
                ", direction." + DBHelper.KEY_DIRECTIONS_NAME + " as " + names[5] +
                ", category." + DBHelper.KEY_CATEGORIES_NAME + " as " + names[6] +
                ", category." + DBHelper.KEY_CATEGORIES_DISCOUNT + " as " + names[7] +
                ", category." + DBHelper.KEY_CATEGORIES_ADDED_VALUE + " as " + names[8] +
                " FROM " + DBHelper.TABLE_NAME_TOURS + " as tour " +
                " INNER JOIN " + DBHelper.TABLE_NAME_HOTELS + " as hotel" +
                " on tour." + DBHelper.KEY_TOURS_ID_HOTEL +
                " = hotel." + DBHelper.KEY_HOTELS_ID +
                " INNER JOIN " + DBHelper.TABLE_NAME_DIRECTIONS + " as direction" +
                " on hotel." + DBHelper.KEY_HOTELS_ID_DIRECTION +
                " = direction." + DBHelper.KEY_DIRECTIONS_ID +
                " INNER JOIN " + DBHelper.TABLE_NAME_CATEGORIES + " as category" +
                " on tour." + DBHelper.KEY_TOURS_ID_CATEGORY +
                " = category." + DBHelper.KEY_CATEGORIES_ID +
                " WHERE tour." + DBHelper.KEY_TOURS_ID +
                " = " + selectedTourID;
        cursor1 = database.rawQuery(query, null);
        if (cursor1.moveToFirst() == true) {
            int indTourName = cursor1.getColumnIndex(names[1]);
            binding.etTourName.setText(cursor1.getString(indTourName));
            int indTourPrice = cursor1.getColumnIndex(names[2]);
            binding.etTourPrice.setText(String.valueOf(cursor1.getFloat(indTourPrice)));
            int indTourStartDate = cursor1.getColumnIndex(names[3]);
            long date = cursor1.getInt(indTourStartDate);
            String day = String.valueOf(date % 100);
            String month = String.valueOf(date /100 % 100);
            String year = String.valueOf(date /10000);
            binding.etTourDateStart.setText(year + "." + month + "." + day);
            int indTourDaysCount = cursor1.getColumnIndex(names[4]);
            binding.etTourDaysCount.setText(String.valueOf(cursor1.getInt(indTourDaysCount)));
            int indDirectionName = cursor1.getColumnIndex(names[5]);
            binding.etTourDirectionName.setText(cursor1.getString(indDirectionName));
            int indCategoryName = cursor1.getColumnIndex(names[6]);
            binding.etTourCategoryName.setText(cursor1.getString(indCategoryName));
            int indCategoryDiscount = cursor1.getColumnIndex(names[7]);
            binding.etTourDiscount.setText(String.valueOf(cursor1.getFloat(indCategoryDiscount)));
            int indCategoryAddedValue = cursor1.getColumnIndex(names[8]);
            binding.etTourAddedValue.setText(String.valueOf(cursor1.getFloat(indCategoryAddedValue)));
        }
    }
    /**
     * Метод заполненния полей клиента на форме путевок
     */
    public void setClient(){
        Log.d(tagDB, "Вызов метода setClient фрагмента NewVoucherFragment");
        if (selectedClientID1 > -1)
            selectedClientID = selectedClientID1;
        database = dbHelper.getReadableDatabase();
        Cursor cursor1 = database.query(DBHelper.TABLE_NAME_CLIENTS,
                null,
                DBHelper.KEY_CLIENTS_ID + " = ?",
                new String[]{
                        String.valueOf(selectedClientID)},
                null,
                null,
                null);
        if (cursor1.moveToFirst() == true) {
            int indName = cursor1.getColumnIndex(DBHelper.KEY_CLIENTS_NAME);
            binding.etClientName.setText(cursor1.getString(indName));
            int indSurname = cursor1.getColumnIndex(DBHelper.KEY_CLIENTS_SURNAME);
            binding.etClientSurname.setText(cursor1.getString(indSurname));
            int indPatronymic = cursor1.getColumnIndex(DBHelper.KEY_CLIENTS_PATRONYMIC);
            binding.etClientPatronymic.setText(cursor1.getString(indPatronymic));
            int indPassport = cursor1.getColumnIndex(DBHelper.KEY_CLIENTS_PASSPORT);
            binding.etClientPassport.setText(cursor1.getString(indPassport));
        }
    }
    /**
     * Метод заполненния полей сотрудника на форме путевок
     */
    public void setEmployee(int id) {
        Log.d(tagDB, "Вызов метода setEmployee фрагмента NewVoucherFragment");
        database = dbHelper.getReadableDatabase();

        Cursor cursor1 = database.query(DBHelper.TABLE_NAME_EMPLOYEES,
                null,
                DBHelper.KEY_KINDS_ID + " = ?",
                new String[]{
                        String.valueOf(id)},
                null,
                null,
                null);
        if (cursor1.moveToFirst() == true) {
            int indName = cursor1.getColumnIndex(DBHelper.KEY_EMPLOYEES_NAME);
            binding.etEmployeeName.setText(cursor1.getString(indName));
            int indSurname = cursor1.getColumnIndex(DBHelper.KEY_EMPLOYEES_SURNAME);
            binding.etEmployeeSurname.setText(cursor1.getString(indSurname));
            int indPatronymic = cursor1.getColumnIndex(DBHelper.KEY_EMPLOYEES_PATRONYMIC);
            binding.etEmployeePatronymic.setText(cursor1.getString(indPatronymic));
        }
    }
    /**
     * Метод заполненния полей путевки
     */
    public void setVoucher() {
        Log.d(tagDB, "Вызов метода setVoucher фрагмента NewVoucherFragment");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = String.valueOf(year) + ".";
        if (month < 10) date += 0;
        date += String.valueOf(month+1) + ".";
        if (day < 10) date += 0;
        date += String.valueOf(day);
        binding.etVoucherDate.setText(date);

        float addedValue = Float.parseFloat(binding.etTourAddedValue.getText().toString());
        float discount = Float.parseFloat(binding.etTourDiscount.getText().toString()) * 0.01f;
        float tourPrice = Float.parseFloat(binding.etTourPrice.getText().toString());
        int daysCount = Integer.parseInt(binding.etTourDaysCount.getText().toString());
        float price = (tourPrice * daysCount) * (1 - discount) + addedValue;
        binding.etVoucherPrice.setText(String.valueOf(price));
    }

    /**
     * Метод для определения наименования путевки
     */
    public void setVoucherName() {
        binding.etVoucherName.setText(
                binding.etVoucherDate.getText().toString() + "." +
                        binding.etTourCategoryName.getText().toString().charAt(0) + "." +
                        binding.etTourDirectionName.getText().toString()
        );
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
     * Метод нажатия на кнопку перехода на фрагмент туров для выбора
     */
    public void onTour(View view){
        Bundle bundle1 = new Bundle();
        bundle1.putString("action", "choose");
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_tour, bundle1);
    }
    /**
     * Метод нажатия на кнопку перехода на фрагмент клиентов для выбора
     */
    public void onClient(View view){
        Bundle bundle1 = new Bundle();
        bundle1.putString("action", "choose");
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_client, bundle1);
    }



    /**
     * Метод добавления записи в БД
     * @param view
     */
    public void onAdd(View view) {
        Log.d(tagDB, "Вызов метода onAdd фрагмента NewVoucherFragment");
        setVoucherName();
        database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_VOUCHERS_NAME, binding.etVoucherName.getText().toString());
        contentValues.put(DBHelper.KEY_VOUCHERS_PRICE, binding.etVoucherPrice.getText().toString());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = String.valueOf(year);
        if (month < 10) date += 0;
        date += String.valueOf(month+1);
        if (day < 10) date += 0;
        date += String.valueOf(day);
        contentValues.put(DBHelper.KEY_VOUCHERS_DATE, date);
        contentValues.put(DBHelper.KEY_VOUCHERS_ID_TOUR, String.valueOf(selectedTourID));
        contentValues.put(DBHelper.KEY_VOUCHERS_ID_CLIENT, String.valueOf(selectedClientID));
        contentValues.put(DBHelper.KEY_VOUCHERS_ID_EMPLOYEE, String.valueOf(Authorisation.id));

        long result = database.insert(DBHelper.TABLE_NAME_VOUCHERS, null, contentValues);
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
        Log.d(tagDB, "Вызов метода onEdit фрагмента NewVoucherFragment");
        database = dbHelper.getWritableDatabase();
        setVoucher();
        setVoucherName();
        setEmployee(Authorisation.id);
        if (rowIsExist(DBHelper.TABLE_NAME_VOUCHERS, DBHelper.KEY_VOUCHERS_ID, selectedID)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_VOUCHERS_NAME, binding.etVoucherName.getText().toString());
            contentValues.put(DBHelper.KEY_VOUCHERS_PRICE, binding.etVoucherPrice.getText().toString());
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String date = String.valueOf(year);
            if (month < 10) date += 0;
            date += String.valueOf(month+1);
            if (day < 10) date += 0;
            date += String.valueOf(day);
            contentValues.put(DBHelper.KEY_VOUCHERS_DATE, date);
            contentValues.put(DBHelper.KEY_VOUCHERS_ID_TOUR, String.valueOf(selectedTourID));
            contentValues.put(DBHelper.KEY_VOUCHERS_ID_CLIENT, String.valueOf(selectedClientID));
            contentValues.put(DBHelper.KEY_VOUCHERS_ID_EMPLOYEE, String.valueOf(Authorisation.id));
            String where = DBHelper.KEY_VOUCHERS_ID + "=" + selectedID;

            long result = database.update(DBHelper.TABLE_NAME_VOUCHERS, contentValues, where, null);
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
        Log.d(tagDB, "Вызов метода onDelete фрагмента NewVoucherFragment");
        database = dbHelper.getWritableDatabase();

        if (rowIsExist(DBHelper.TABLE_NAME_VOUCHERS, DBHelper.KEY_VOUCHERS_ID, selectedID)) {
            long result = database.delete(dbHelper.TABLE_NAME_VOUCHERS,
                    DBHelper.KEY_VOUCHERS_ID + " = ?", new String[]{String.valueOf(selectedID)});
            if (result > 0) {
                Log.d(tagDB, getResources().getString(R.string.action_result_OK));
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_delete_result_OK), Toast.LENGTH_LONG);
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
}