package com.strongest.savingdata.Fragments;

public interface OnProgramSettingChange {

    void onDelete(int position);
    void onSwap(int fromPosition, int toPosition);
    void onEdit(int position);
}
