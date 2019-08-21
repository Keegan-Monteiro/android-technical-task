package com.example.minimoneybox;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {
    final private ItemClickListener mItemClickListener;
    private Context mContext;
    private List<Account> mAccounts;

    //receive object that handles implements the item click interface
    public AccountAdapter(Context context, ItemClickListener itemClickListener) {
        mContext = context;
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //get the list item layout
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.account_list, viewGroup, false);

        return new AccountViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        //set the text for the views
        holder.accountNameTextView.setText(mAccounts.get(position).getFriendlyName());
        holder.planValueTextView.setText(mAccounts.get(position).getPlanValueString(mContext));
        holder.moneyBoxTextView.setText(mAccounts.get(position).getMoneyBoxString(mContext));
    }

    public List<Account> getAccounts() {
        return mAccounts;
    }

    void setAccounts(List<Account> accounts){
        mAccounts = accounts;
        notifyDataSetChanged();
    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        int listSize;

        //The dataset may not be initialised yet so check first!
        if (mAccounts == null) {
            listSize = 0;
        } else {
            listSize = mAccounts.size();
        }

        return listSize;
    }

    //create interface that handles the listener
    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView accountNameTextView;
        public TextView planValueTextView;
        public TextView moneyBoxTextView;

        public AccountViewHolder(View view) {
            super(view);

            //assign the views to the variable
            accountNameTextView = view.findViewById(R.id.ua_account_name_textview);
            planValueTextView = view.findViewById(R.id.ua_plan_value_textview);
            moneyBoxTextView = view.findViewById(R.id.ua_moneybox_textview);

            //set the click listener
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //get the task id
            int elementId = getAdapterPosition();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}
