package com.example.cursework.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.cursework.R;
import com.example.cursework.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) { ;

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.btnDirections.setOnClickListener(this::onDirection);
        binding.btnHotels.setOnClickListener(this::onHotel);
        binding.btnKinds.setOnClickListener(this::onKind);
        binding.btnCategories.setOnClickListener(this::onCategory);
        binding.btnTourOperators.setOnClickListener(this::onTourOperator);
        binding.btnEmployees.setOnClickListener(this::onEmployee);
        binding.btnClients.setOnClickListener(this::onClient);
        binding.btnTours.setOnClickListener(this::onTour);
        binding.btnVouchers.setOnClickListener(this::onVoucher);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Метод для навигации к фрагменту Направлений
     * @param view
     */
    public void onDirection(View view){
        Bundle bundle = new Bundle();
        bundle.putString("action", "view");
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_direction, bundle);
    }
    /**
     * Метод для навигации к фрагменту Отелей
     * @param view
     */
    public void onHotel(View view){
        Bundle bundle = new Bundle();
        bundle.putString("action", "view");
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_hotel, bundle);
    }
    /**
     * Метод для навигации к фрагменту Видов туров
     * @param view
     */
    public void onKind(View view){
        Bundle bundle = new Bundle();
        bundle.putString("action", "view");
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_kinds, bundle);
    }
    /**
     * Метод для навигации к фрагменту Категорий туров
     * @param view
     */
    public void onCategory(View view){
        Bundle bundle = new Bundle();
        bundle.putString("action", "view");
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_category, bundle);
    }
    /**
     * Метод для навигации к фрагменту Туроператоров
     * @param view
     */
    public void onTourOperator(View view){
        Bundle bundle = new Bundle();
        bundle.putString("action", "view");
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_tour_operator, bundle);
    }
    /**
     * Метод для навигации к фрагменту Сотрудников
     * @param view
     */
    public void onEmployee(View view){
        Bundle bundle = new Bundle();
        bundle.putString("action", "view");
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_employee, bundle);
    }
    /**
     * Метод для навигации к фрагменту Клиентов
     * @param view
     */
    public void onClient(View view){
        Bundle bundle = new Bundle();
        bundle.putString("action", "view");
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_client, bundle);
    }
    /**
     * Метод для навигации к фрагменту Туров
     * @param view
     */
    public void onTour(View view){
        Bundle bundle = new Bundle();
        bundle.putString("action", "view");
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_tour, bundle);
    }
    /**
     * Метод для навигации к фрагменту Путевок
     * @param view
     */
    public void onVoucher(View view){
        Bundle bundle = new Bundle();
        bundle.putString("action", "view");
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_voucher, bundle);
    }
}