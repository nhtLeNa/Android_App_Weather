package com.example.myweather.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myweather.Activities.CovidStatusCountryActivity;
import com.example.myweather.Model.CovidCountry;
import com.example.myweather.R;

import java.util.ArrayList;

public class CovidStatusCountryAdapter extends RecyclerView.Adapter<CovidStatusCountryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CovidCountry> arrayList;
    private CovidStatusCountryAdapter.OnItemClickListner onItemClickListner;

    private static final String NAME = "NAME";
    private static final String CASE = "CASE";
    private static final String TODAYCASE = "TODAYCASE";
    private static final String DEATH = "DEATH";
    private static final String TODAYDEATH = "TODAYDEATH";
    private static final String RECOVERED = "RECOVERED";
    private static final String ACTIVE = "ACTIVE";
    private static final String FLAG = "FLAG";

    public interface OnItemClickListner {
        void onItemClick(int position);
    }

    public void setOnItemClickListner(OnItemClickListner listner) {
        onItemClickListner = listner;
    }


    public CovidStatusCountryAdapter(Context c, ArrayList<CovidCountry> countryModelArrayList) {
        context = c;
        arrayList = countryModelArrayList;
    }

    @NonNull
    @Override
    public CovidStatusCountryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.country_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CovidStatusCountryAdapter.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                CovidCountry clickedItem = arrayList.get(position);
                Intent perCountryIntent = new Intent(context, CovidStatusCountryActivity.class);

                perCountryIntent.putExtra(NAME, clickedItem.getCountry());
                perCountryIntent.putExtra(ACTIVE, clickedItem.getActive());
                perCountryIntent.putExtra(CASE, clickedItem.getCases());
                perCountryIntent.putExtra(TODAYCASE, clickedItem.getTodayCases());
                perCountryIntent.putExtra(DEATH, clickedItem.getDeaths());
                perCountryIntent.putExtra(TODAYDEATH, clickedItem.getTodayDeaths());
                perCountryIntent.putExtra(RECOVERED, clickedItem.getRecovered());
                perCountryIntent.putExtra(FLAG, clickedItem.getFlag());

                Log.d("data20", "onClick:");
                Log.d("data2", "onClick:" + clickedItem.getCountry().toString() + "B");

                context.startActivity(perCountryIntent);
            }
        });

        CovidCountry currentItem = arrayList.get(position);
        String countryName = currentItem.getCountry();
        String countryFlag = currentItem.getFlag();
        holder.countryName.setText(countryName);
        Glide.with(context).load(countryFlag).into(holder.flagImage);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void filterList(ArrayList<CovidCountry> filteredList) {
        arrayList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView countryName;
        ImageView flagImage;

        public ViewHolder(View itemView) {
            super(itemView);

            countryName = itemView.findViewById(R.id.txtCountryName);
            flagImage = itemView.findViewById(R.id.imgCountryFlag);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListner.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
