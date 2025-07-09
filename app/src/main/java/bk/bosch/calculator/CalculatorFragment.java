package bk.bosch.calculator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bk.bosch.calculator.databinding.FragmentCalculatorBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalculatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalculatorFragment extends Fragment implements MyAdapter.OnButtonClickListener{
    private FragmentCalculatorBinding binding;
    private CalculatorViewModel viewModel;
    private RecyclerView buttonsRecyclerView;
    private MyAdapter adapter;

    public CalculatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalculatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalculatorFragment newInstance() {
        return new CalculatorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CalculatorViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout using data binding
        binding = FragmentCalculatorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Bind ViewModel to layout
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // Setup RecyclerView
        buttonsRecyclerView = binding.buttonsRecyclerview;
        setupRecyclerView();
    }
    private void setupRecyclerView() {
        adapter = new MyAdapter(viewModel.getButtons(), this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        buttonsRecyclerView.setLayoutManager(gridLayoutManager);
        buttonsRecyclerView.setAdapter(adapter);
    }
    @Override
    public void onButtonClick(String buttonText, MyButton.ButtonType buttonType) {
        viewModel.onButtonClick(buttonText, buttonType);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}