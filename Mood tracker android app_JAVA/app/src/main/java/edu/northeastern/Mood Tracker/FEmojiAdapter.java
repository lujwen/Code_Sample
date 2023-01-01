package edu.northeastern.mobileapplicationteam18;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FEmojiAdapter extends RecyclerView.Adapter<FEmojiViewHolder> {
    private final List<Emoji> Emojis;

    public FEmojiAdapter(List<Emoji> Emojis) {
        this.Emojis = Emojis;
    }

    @NonNull
    @Override
    public FEmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FEmojiViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emoji_received_header, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FEmojiViewHolder holder, int position) {
        holder.bindThisData(Emojis.get(position));
    }

    @Override
    public int getItemCount() {
        if (Emojis != null) {
            return Emojis.size();
        } else {
            return 0;
        }
    }
}
