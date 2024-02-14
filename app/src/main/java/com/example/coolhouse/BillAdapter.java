package com.example.coolhouse;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder>{

    private List<Bill> billList;
    private Context context;

    public BillAdapter(Context context, List<Bill> billList) {
        this.context = context;
        this.billList = billList;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View billView = inflater.inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(billView);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = billList.get(position);

        // Update TextViews or UI components with bill details
        holder.billDetailsTextView.setText("Bill Details: " + bill.getDetails());

        // Set click listener for delete button
        holder.deleteBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showConfirmationDialog(holder.getAdapterPosition());
//                int adapterPosition = holder.getAdapterPosition();
//                if (adapterPosition != RecyclerView.NO_POSITION) {
//                    billList.remove(adapterPosition);
//                    // Notify the adapter that the data set has changed
//                    notifyItemRemoved(adapterPosition);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    // ViewHolder class
    public static class BillViewHolder extends RecyclerView.ViewHolder {
        public TextView billDetailsTextView;
        public Button deleteBillButton;

        public BillViewHolder(View itemView) {
            super(itemView);

            // Your view should match the layout item_bill.xml
            billDetailsTextView = itemView.findViewById(R.id.billDetailsTextView);
            deleteBillButton = itemView.findViewById(R.id.deleteBillButton);
        }
    }
    private void showConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this bill?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User confirmed, delete the bill
                        deleteBill(position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User canceled, do nothing
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void deleteBill(int position) {
        // Remove the bill from the list
        billList.remove(position);
        // Notify the adapter that the data set has changed
        notifyItemRemoved(position);
    }

    public void setBillList(List<Bill> billList) {
        this.billList = billList;
        notifyDataSetChanged();
    }
}
