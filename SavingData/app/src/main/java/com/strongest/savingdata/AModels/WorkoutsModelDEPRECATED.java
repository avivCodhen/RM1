package com.strongest.savingdata.AModels;

/*

public class WorkoutsModel extends ViewModel implements OnWorkoutViewInterfaceClicksListener {
    
    private Context context;
    private ArrayList<PLObject> exArray;

    public WorkoutsModel(Context context, ArrayList<PLObject> exArray){
        this.context = context;

        this.exArray = exArray;
    }

    @Override
    public void saveProgram() {

    }

    @Override
    public void notifyExerciseChanged(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onExerciseClick(MyExpandableAdapter.ExerciseViewHolder vh) {
        PLObject.ExerciseProfile exerciseProfile = (PLObject.ExerciseProfile) exArray.get(vh.getAdapterPosition());
        if (exerciseProfile.isMore()) {
            vh.card.animate().x(0);
            exerciseProfile.setMore(false);
        }
        if (exerciseProfile.isExpand()) {
            collapseExercise(vh);
        } else {
            expandExerciseSupersets(vh);
        }
    }

    @Override
    public void onLongSupersetClick(final int position, boolean delete) {
        PLObject.ExerciseProfile ep = null;
        ep = ((PLObject.ExerciseProfile) exArray.get(position));
        if (delete) {
            int length = ep.getParent().getExerciseProfiles().size() + ep.getParent().getSets().size();
            boolean isExpand = ep.getParent().isExpand();

            ep.getParent().getExerciseProfiles().remove(ep);
            LayoutManager.UpdateComponents updateComponents = new LayoutManager.UpdateComponents(DELETE_EXERCISE);
            updateComponents.setPlObject(ep);
            updateComponents.setRemovePosition(position);
            exArray = onUpdateLayoutStatsListener.updateLayout(updateComponents);

            adapter.setExArray(exArray);
            adapter.notifyItemRemoved(position);
            if (isExpand) {

                for (int i = position; i < exArray.size(); i++) {
                    for (int j = 0; j < ep.getParent().getSets().size(); j++) {
                        if (exArray.get(i).type == WorkoutLayoutTypes.IntraSet && ep.getIntraSets().get(j) == exArray.get(i)) {
                            exArray.remove(i);
                            adapter.notifyItemRemoved(i);
                        }
                    }
                }
            }
            int start = 0;
            int end = 0;
            int fatherPos = LayoutManagerHelper.findPLObjectPosition(ep.getParent(), exArray);
            adapter.notifyItemRangeChanged(fatherPos, LayoutManagerHelper.calcBlockLength(ep.getParent()));
            //adapter.notifyItemRangeRemoved(position+start, ep.getParent().getSets().size()*2);

            return;
        }
    }

    @Override
    public void onLongClick(final RecyclerView.ViewHolder vh, boolean delete) {
        String item = null;
        View v = null;

        longClickMenu.onShowMenu((MyExpandableAdapter.MyExpandableViewHolder) vh,
                LayoutManagerHelper.findPLObjectDefaultType(exArray.get(vh.getAdapterPosition())), delete);

    }

    @Override
    public void onBodyViewLongClick(final RecyclerView.ViewHolder vh, boolean delete) {
        if (delete) {
            exArray.remove(vh.getAdapterPosition());
            adapter.notifyItemRemoved(vh.getAdapterPosition());
        }
        new AlertDialog.Builder(context)
                .setCancelable(true)
                .setMessage("Delete this Exercise and all it's data?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onLongClick(vh, true);

                    }
                }).setNegativeButton("No", null).show();
    }

    @Override
    public void collapseExercise(int adapterPosition) {
        if (lm.findFirstVisibleItemPosition() <= adapterPosition) {
            MyExpandableAdapter.ExerciseViewHolder vh =
                    (MyExpandableAdapter.ExerciseViewHolder) recycler.findViewHolderForLayoutPosition(adapterPosition);
            collapseExercise(vh);
        }

    }

    @Override
    public void expandExerciseSupersets(int adapterPosition) {
        MyExpandableAdapter.ExerciseViewHolder vh = (MyExpandableAdapter.ExerciseViewHolder) recycler.findViewHolderForLayoutPosition(adapterPosition);
        expandExerciseSupersets(vh);
    }

    @Override
    public void onScrollPosition(int position, boolean flag1, boolean flag2) {

    }

    @Override
    public void onLongClickMenuAddSuperset(final RecyclerView.ViewHolder vh) {
        if (lm.findFirstCompletelyVisibleItemPosition() < vh.getAdapterPosition()) {
            scrollToPosition(vh.getAdapterPosition(), true, true);
        }
        PLObject.ExerciseProfile ep = (PLObject.ExerciseProfile) exArray.get(vh.getAdapterPosition());
        PLObject.ExerciseProfile superset = new PLObject.ExerciseProfile(null, 0, 0, 0);
        superset.setInnerType(WorkoutLayoutTypes.IntraExerciseProfile);
        superset.setParent(ep);
        if (ep.isExpand()) {
            collapseExercise((MyExpandableAdapter.ExerciseViewHolder) vh);
            //   onCollapse(vh);
        }
        for (int i = 0; i < ep.getSets().size(); i++) {
            superset.getIntraSets().add(new PLObject.IntraSetPLObject(superset, new ExerciseSet(),
                    WorkoutLayoutTypes.SuperSetIntraSet, null));
        }
        ep.getExerciseProfiles().add(superset);
        LayoutManager.UpdateComponents up = new LayoutManager.UpdateComponents(NEW_SUPERSET);
        up.setToPosition(vh.getAdapterPosition() + ep.getExerciseProfiles().size());
        up.setPlObject(superset);
        exArray = onUpdateLayoutStatsListener.updateLayout(up);
        // exArray.add(vh.getAdapterPosition() + ep.getExerciseProfiles().size(), superset);
        adapter.setExArray(exArray);
        adapter.notifyItemInserted(vh.getAdapterPosition() + ep.getExerciseProfiles().size());
    }

    @Override
    public void onLongClickHideMenu() {
        longClickMenu.onHideMenu();
    }

    @Override
    public void onAddNormalIntraSet(RecyclerView.ViewHolder vh) {
        PLObject.SetsPLObject setsPLObject;
        PLObject.IntraSetPLObject intra;
        int addPosition = -1;
        if (exArray.get(vh.getAdapterPosition()) instanceof PLObject.IntraSetPLObject) {
            intra = (PLObject.IntraSetPLObject) exArray.get(vh.getAdapterPosition());
            setsPLObject = intra.getParentSet();
            addPosition = vh.getAdapterPosition() + 1;
        } else {
            setsPLObject = (PLObject.SetsPLObject) exArray.get(vh.getAdapterPosition());
            addPosition = vh.getAdapterPosition() + setsPLObject.getIntraSets().size() + 1;

        }
        intra = layoutManagerOperator.injectNewIntraSet(setsPLObject);
        if (addPosition != -1) {
            exArray.add(addPosition, intra);
            adapter.notifyItemInserted(addPosition);
        }
        int parentposition = LayoutManagerHelper.findPLObjectPosition(setsPLObject.getParent(), exArray);
        adapter.notifyItemChanged(parentposition);
        layoutManager.saveLayoutToDataBase(true);
    }

    @Override
    public void deleteItem(int position, boolean delete) {
        if (delete) {
            PLObject plObject = exArray.get(position);
            WorkoutLayoutTypes type = LayoutManagerHelper.findPLObjectDefaultType(plObject);
            switch (type) {
                case IntraExerciseProfile:
                    onLongSupersetClick(position, delete);
                    //layoutManagerOperator.deleteSuperset((ExerciseProfile) plObject, onUpdateLayoutStatsListener,this);
                    break;
                case ExerciseProfile:
                    deleteExercise(position);
                    // layoutManagerOperator.deleteExercise((ExerciseProfile) plObject, onUpdateLayoutStatsListener, this);
                    break;
                case SetsPLObject:
                    layoutManagerOperator.deleteSet((PLObject.SetsPLObject) plObject, exArray);
                    break;
                case IntraSetNormal:
                    adapter.notifyItemRemoved(
                            layoutManagerOperator.deleteIntraSet((PLObject.IntraSetPLObject) plObject, exArray)
                    );
                    int parentposition = LayoutManagerHelper.findPLObjectPosition(((PLObject.IntraSetPLObject) plObject).getParent(), exArray);
                    adapter.notifyItemChanged(parentposition);
                    break;
                case BodyView:
                    exArray.remove(position);
                    adapter.notifyItemRemoved(position);
            }

            longClickMenu.onHideMenu();
            return;
        }
        LayoutManagerAlertdialog.getAlertDialog(context, this, position);

    }

    @Override
    public void deleteExercise(int position) {
        PLObject.ExerciseProfile ep = (PLObject.ExerciseProfile) exArray.get(position);
        int length = 1;
        if (ep.isExpand()) {
            collapseExercise(position);
            //adapter.onCollapse((MyExpandableAdapter.ExerciseViewHolder) vh);
        }
        LayoutManager.UpdateComponents updateComponents = new LayoutManager.UpdateComponents(DELETE_EXERCISE);
        updateComponents.setPlObject(ep);
        updateComponents.setRemovePosition(position);
        updateComponents.setLayout(exArray);
        exArray = onUpdateLayoutStatsListener.updateLayout(updateComponents);
        adapter.setExArray(exArray);
        adapter.notifyItemRangeRemoved(position, ep.getExerciseProfiles().size() + 1);
        return;
    }

    @Override
    public void collapseExercise(MyExpandableAdapter.ExerciseViewHolder vh) {
        int adapterPosition = vh.getAdapterPosition();


        PLObject.ExerciseProfile ep = (PLObject.ExerciseProfile) exArray.get(adapterPosition);
        int block = LayoutManagerHelper.calcBlockLength(ep);
        if (longClickMenu.getViewPosition() > adapterPosition && longClickMenu.getViewPosition() < block + 1) {
            longClickMenu.onHideMenu();
        }
        ArrayList<PLObject.SetsPLObject> sets = ep.getSets();
        int newPosition = ep.getExerciseProfiles().size() + 1;
        int count = 0;
        if (sets != null) {
            for (int i = 0; i < sets.size(); i++) {
                exArray.remove(adapterPosition + newPosition);
                count++;

                for (int k = 0; k < sets.get(i).getIntraSets().size(); k++) {
                    exArray.remove(adapterPosition + newPosition);
                    count++;

                }


                if (ep.getExerciseProfiles().size() != 0) {
                    for (int j = 0; j < ep.getExerciseProfiles().size(); j++) {
                        exArray.remove(adapterPosition + newPosition);
                        count++;
                    }
                }


            }
            adapter.notifyItemRangeRemoved(adapterPosition + newPosition, count);
            scrollToPosition(adapterPosition, true, true);

        }
        ep.setExpand(false);
        adapter.animateCollapse(vh);
        vh.expand = false;

    }

    @Override
    public void expandExerciseSupersets(MyExpandableAdapter.ExerciseViewHolder vh) {
        int adapterPosition = vh.getAdapterPosition();
        PLObject.ExerciseProfile ep = (PLObject.ExerciseProfile) exArray.get(adapterPosition);
        ArrayList<PLObject.SetsPLObject> sets = ep.getSets();
        ArrayList<PLObject> block = new ArrayList<>();

        int newPosition = ep.getExerciseProfiles().size() + 1;
        int row = 0;
        if (sets != null) {
            for (int i = 0; i < sets.size(); i++) {
                block.add(sets.get(i));

                for (int j = 0; j < sets.get(i).getIntraSets().size(); j++) {
                    ++row;

                    if (sets.get(i).getIntraSets().size() != 0) {
                        block.add(sets.get(i).getIntraSets().get(j));
                        ++row;

                    }
                    //exArray.add(position + i + newPosition, obj);
                }

                if (ep.getExerciseProfiles().size() != 0) {
                    for (int j = 0; j < ep.getExerciseProfiles().size(); j++) {
                        LayoutManagerHelper.attachSupersetIntraSetsByParent(ep.getExerciseProfiles().get(j));
                        block.add(ep.getExerciseProfiles().get(j).getIntraSets().get(i));

                    }
                }
                ++row;

            }
            int i = 0;
            for (PLObject plObject : block) {
                exArray.add(adapterPosition + newPosition + i, plObject);
                adapter.notifyItemInserted(adapterPosition + newPosition + i);
                i++;
            }
            //  adapter.notifyItemRangeInserted(adapterPosition + newPosition, row);
            scrollToPosition(adapterPosition, true, true);
        }
        ep.setExpand(true);
        adapter.animateExpand(vh);
        vh.expand = true;
    }

    @Override
    public void onSetsDoubleClick(final RecyclerView.ViewHolder vh) {
        int position = vh.getAdapterPosition();
        PLObject plObject = exArray.get(position);
        PLObject.SetsPLObject setsPLObject;
        if (plObject instanceof PLObject.SetsPLObject || plObject.getInnerType() == WorkoutLayoutTypes.ExerciseProfile) {

            boolean returnToMenu = false;
            if (plObject.getInnerType() == WorkoutLayoutTypes.ExerciseProfile) {
                PLObject.ExerciseProfile ep = (PLObject.ExerciseProfile) plObject;
                if (!ep.isExpand()) {
                    expandExerciseSupersets(position);
                }
                position += (LayoutManagerHelper.calcBlockLength(ep));
                returnToMenu = true;
                setsPLObject = ep.getSets().get(ep.getSets().size() - 1);
                if (setsPLObject.getIntraSets().size() > 0) {
                    position--;
                }
                position -= setsPLObject.getParent().getExerciseProfiles().size();

            } else {
                longClickMenu.onHideMenu();
                setsPLObject = (PLObject.SetsPLObject) exArray.get(position);
                position++;
            }
            int setBlockLength = setsPLObject.getIntraSets().size() + setsPLObject.getParent().getExerciseProfiles().size();
            int newPosition = position + setBlockLength;
            ArrayList<PLObject> block = layoutManagerOperator.injectCopySet(setsPLObject);
            for (int i = 0; i < block.size(); i++) {
                if (exArray.size() <= newPosition + i) {
                    exArray.add(block.get(i));
                } else {
                    exArray.add(newPosition + i, block.get(i));
                }
                adapter.notifyItemInserted(newPosition + i);
            }
            int numOfBlock = 0;
            for (int i = position; i < exArray.size(); i++) {
                if (exArray.get(i).getType() != WorkoutLayoutTypes.ExerciseProfile) {
                    numOfBlock++;
                } else {
                    break;
                }
            }
            adapter.notifyItemRangeChanged(position + 1, numOfBlock);
            if (!returnToMenu) {
                notifyExerciseChanged(LayoutManagerHelper.findPLObjectPosition(setsPLObject.getParent(), exArray));
            }
            layoutManager.saveLayoutToDataBase(true);
        }

    }

    @Override
    public void onExerciseDoubleClick(PLObject.ExerciseProfile ep, int position) {

    }

    @Override
    public void onSetsLongClick(final RecyclerView.ViewHolder vh, final int childPosition, boolean delete) {
        int position = vh.getAdapterPosition();
        int count = 0;
        PLObject.SetsPLObject setsPLObject = (PLObject.SetsPLObject) exArray.get(position);
        if (setsPLObject.getParent().getSets().size() == 1) {
            Toast.makeText(context, "You cannot delete the only set.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (delete) {
            setsPLObject.getParent().getSets().remove(childPosition);
            for (int i = 0; i < setsPLObject.getParent().getExerciseProfiles().size(); i++) {
                count++;
                setsPLObject.getParent().getExerciseProfiles().get(i).getIntraSets().remove(childPosition);
                exArray.remove(position);
            }
            for (int i = 0; i < setsPLObject.getIntraSets().size() + 1; i++) {
                exArray.remove(position);
                count++;
            }
            int end = 0;
            for (int i = position; i < exArray.size(); i++) {
                if (exArray.get(i) instanceof PLObject.ExerciseProfile) {
                    break;
                } else {
                    end++;
                }
            }
            adapter.notifyItemRangeRemoved(position, count);
            adapter.notifyItemRangeChanged(position, end);
            return;
        }

    }

    @Override
    public void onMoreClick(RecyclerView.ViewHolder vh) {
        View v = null;
        View expandV = null;
        if (vh instanceof MyExpandableAdapter.ExerciseViewHolder) {
            v = ((MyExpandableAdapter.ExerciseViewHolder) vh).card;
            expandV = ((MyExpandableAdapter.ExerciseViewHolder) vh).expandableLayout;
        } else if (vh instanceof MyExpandableAdapter.SetsViewHolder) {
            v = ((MyExpandableAdapter.SetsViewHolder) vh).card;
            // expandV = ((MyExpandableAdapter.SetsViewHolder) vh).expandableLayout;


        }
        PLObject plObject = exArray.get(vh.getAdapterPosition());
        if (plObject.isMore()) {
            v.animate().x(0);

            plObject.setMore(false);
        } else {
            plObject.setMore(true);
            v.animate().x(-expandV.getWidth());
        }
    }

    @Override
    public void onSettingsClick(RecyclerView.ViewHolder vh) {
        View v = null;
        if (vh instanceof MyExpandableAdapter.ExerciseViewHolder) {
            v = ((MyExpandableAdapter.ExerciseViewHolder) vh).card;
            PLObject.ExerciseProfile ep = (PLObject.ExerciseProfile) exArray.get(vh.getAdapterPosition());
            ep.rawPosition = LayoutManagerHelper.findExercisePosition(ep, exArray);

        } else if (vh instanceof MyExpandableAdapter.SetsViewHolder) {
            v = ((MyExpandableAdapter.SetsViewHolder) vh).card;
        }

        PLObject plObject = exArray.get(vh.getAdapterPosition());
        if (plObject.isMore()) {
            v.animate().x(0);
            plObject.setMore(false);

        }
        if (plObject.isEditMode()) {
            adapter.detailCollapse(vh.getAdapterPosition());
            plObject.setEditMode(false);
            // exerciseProfile.setEditMode(false);

        } else {
            plObject.setEditMode(true);
            //  exerciseProfile.setEditMode(true);
            adapter.deTailExpand(vh);
        }
    }

    @Override
    public void onSwapExercise(int fromPosition, int toPosition) {
        layoutManager.saveLayoutToDataBase(true);
    }



}
*/
