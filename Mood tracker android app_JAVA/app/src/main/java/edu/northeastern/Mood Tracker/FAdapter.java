package edu.northeastern.mobileapplicationteam18;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


    public class FAdapter extends RecyclerView.Adapter<FAdapter.RecyclerViewHolder>{
        private Context mContext;
        private List<FActivity> factivity;
        //private OnItemClickListener mListener;
        private OnItemClickListener mListener;

        public FAdapter(Context context, List<FActivity> uploads) {
            mContext = context;
            factivity = uploads;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.rec0_single_item_row, parent, false);
            return new RecyclerViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            FActivity currentTeacher = factivity.get(position);
            holder.nameTextView.setText(currentTeacher.getName());
            holder.descriptionTextView.setText(currentTeacher.getDescription());
            Picasso.with(mContext)
                    .load(currentTeacher.getImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(holder.teacherImageView);
        }

        @Override
        public int getItemCount() {
            return factivity.size();
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
                View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

            public TextView nameTextView,descriptionTextView,dateTextView;
            public ImageView teacherImageView;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                nameTextView =itemView.findViewById ( R.id.nameTextView );
                descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
                dateTextView = itemView.findViewById(R.id.dateTextView);
                teacherImageView = itemView.findViewById(R.id.teacherImageView);

                itemView.setOnClickListener(this);
                itemView.setOnCreateContextMenuListener(this);
            }

            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(position);
                    }
                }
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Select Action");
                //MenuItem showItem = menu.add( Menu.NONE, 1, 1, "Show");
                MenuItem deleteItem = menu.add(Menu.NONE, 2, 2, "Delete");

                //showItem.setOnMenuItemClickListener(this);
                deleteItem.setOnMenuItemClickListener(this);
            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        switch (item.getItemId()) {
//                            case 1:
//                                mListener.onShowItemClick(position);
//                                return true;
                            case 2:
                                mListener.onDeleteItemClick(position);
                                return true;
                        }
                    }
                }
                return false;
            }
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
            // void onShowItemClick(int position);
            void onDeleteItemClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }
        private String getDateToday(){
            DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
            Date date=new Date();
            String today= dateFormat.format(date);
            return today;
        }
    }