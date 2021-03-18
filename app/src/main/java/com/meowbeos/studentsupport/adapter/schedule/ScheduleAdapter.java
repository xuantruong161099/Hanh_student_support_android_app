package com.meowbeos.studentsupport.adapter.schedule;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.meowbeos.studentsupport.R;
import com.meowbeos.studentsupport.fragment.marks.SubjectFragment;
import com.meowbeos.studentsupport.model.Schedule;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    Context context;
    List<Schedule> scheduleList;

    public ScheduleAdapter(Context context, List<Schedule> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_schedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtSessonsTest.setText("Tiết học: " + scheduleList.get(position).getLessonsTest());
        holder.txtSubjectsName.setText("Môn học: " + scheduleList.get(position).getSubjectsName());
        holder.txtClassRooms.setText("Phòng học: " + scheduleList.get(position).getClassRooms());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                SubjectFragment fragment = new SubjectFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ID", String.valueOf(scheduleList.get(position).getIdSchedule()));
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSessonsTest, txtSubjectsName, txtClassRooms;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSessonsTest = itemView.findViewById(R.id.txtSessonsTest);
            txtSubjectsName = itemView.findViewById(R.id.txtSubjectsName);
            txtClassRooms = itemView.findViewById(R.id.txtClassRooms);
            linearLayout = itemView.findViewById(R.id.linear_layout_Schedule);
        }
    }
}
