package com.example.te_leasetracker_c868.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.te_leasetracker_c868.DB_Entities.Car;
import com.example.te_leasetracker_c868.R;
import com.example.te_leasetracker_c868.Utility.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarHolder> {

    private List<Car> carList = new ArrayList<>();
    private onCarClickListener listener;

    class CarHolder extends RecyclerView.ViewHolder{
        private TextView tvLeaseStart, tvCarName, tvCarMake, tvCarModel;


        CarHolder(@NonNull View itemView) {
            super(itemView);

            ImageButton ibCarEdit = itemView.findViewById(R.id.btn_car_edit);

            tvCarName = itemView.findViewById(R.id.tv_car_name);
            tvLeaseStart = itemView.findViewById(R.id.tv_lease_start);
            tvCarMake = itemView.findViewById(R.id.tv_car_make);
            tvCarModel = itemView.findViewById(R.id.tv_car_model);


            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(listener!=null&&position!=RecyclerView.NO_POSITION){
                    listener.onCarClick(carList.get(position));
                }
            });

            ibCarEdit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(listener!=null&&position!=RecyclerView.NO_POSITION){
                    listener.onEditClick(carList.get(position));
                }
            });

        }
    }

    @NonNull
    @Override
    public CarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_item, parent, false);
        return new CarHolder(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull CarHolder holder, int position) {
        Car currentCar = carList.get(position);
        String leaseStartDate = DateUtil.dateToString(currentCar.getLease_start());
        String carMake = currentCar.getCar_make();
        String carModel = currentCar.getCar_model();
        String carName = currentCar.getCar_name();
        holder.tvCarName.setText(carName);
        holder.tvLeaseStart.setText(leaseStartDate);
        holder.tvCarMake.setText(carMake);
        holder.tvCarModel.setText(carModel);
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public void setCarList(List<Car> cars){
        this.carList = cars;
        notifyDataSetChanged();
    }

    public Car getCarAt(int position){
        return carList.get(position);
    }


    public interface onCarClickListener{
        void onCarClick(Car car);
        void onEditClick(Car car);
    }

    public void setOnCarClickListener(onCarClickListener listener){
        this.listener = listener;
    }
}
