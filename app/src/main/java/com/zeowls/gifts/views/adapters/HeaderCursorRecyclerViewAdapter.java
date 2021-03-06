package com.zeowls.gifts.views.adapters;

/**
 * Created by root on 4/20/16.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.zeowls.gifts.views.adapters.CursorRecyclerViewAdapter;

import java.util.List;

/**
 * Created by tuanchauict on 11/4/15.
 */
public abstract class HeaderCursorRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends
        CursorRecyclerViewAdapter<VH> {

    public static final int TYPE_HEADER = -1;
    public static final int TYPE_NORMAL_ITEM = 0;

    public static class Section {
        public int itemCount;
        public String text;

        /**
         * @param itemCount number item of section. Last section can be anything.
         * @param text
         */
        public Section(int itemCount, String text) {
            this.itemCount = itemCount;
            this.text = text;
        }
    }

    private SparseArray<String> mSectionsIndexer;

    public HeaderCursorRecyclerViewAdapter(Context context, Cursor cursor, List<Section> sections) {
        super(context, cursor);
        mSectionsIndexer = new SparseArray<>();
        convertSectionList(sections);
    }

    private void convertSectionList(List<Section> sections) {
        mSectionsIndexer.clear();
        if (sections != null) {
            int count = 0;
            for (Section section : sections) {
                mSectionsIndexer.put(count, section.text);
                count += section.itemCount + 1;
            }
        }
    }

    public void changeCursor(Cursor cursor, List<Section> sections) {
        super.changeCursor(cursor);
        convertSectionList(sections);
    }

    public void setSections(List<Section> sections) {
        convertSectionList(sections);
    }

    /**
     * If you have to custom this function, remember to avoid return the value of TYPE_HEADER (-1) for none header position.
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (mSectionsIndexer.indexOfKey(position) >= 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_NORMAL_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        if (viewHolder.getItemViewType() == TYPE_HEADER) {
            onBindSectionHeaderViewHolder(viewHolder, mSectionsIndexer.get(position));
        } else {
            getCursor().moveToPosition(position - countNumberSectionsBefore(position));
            onBindItemViewHolder(viewHolder, getCursor());
        }
    }

    private int countNumberSectionsBefore(int position) {
        int count = 0;
        for (int i = 0; i < mSectionsIndexer.size(); i++) {
            if (position > mSectionsIndexer.keyAt(i))
                count++;
        }

        return count;
    }

    public abstract void onBindSectionHeaderViewHolder(VH headerHolder, String header);

    public abstract void onBindItemViewHolder(VH itemHolder, Cursor cursor);

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return onCreateSectionHeaderViewHolder(parent);
        } else {
            return onCreateItemViewHolder(parent, viewType);
        }
    }

    public abstract VH onCreateSectionHeaderViewHolder(ViewGroup parent);

    public abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    @Override
    public final void onBindViewHolder(VH viewHolder, Cursor cursor) {
        //do nothing
    }

    @Override
    public int getItemCount() {
        int count = super.getItemCount();
        if (count > 0) {
            return count + mSectionsIndexer.size();
        } else {
            return 0;
        }
    }


}
