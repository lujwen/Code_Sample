package edu.northeastern.mobileapplicationteam18;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiViewHolder> {
    private final List<Emoji> Emojis;

    public EmojiAdapter(List<Emoji> Emojis) {
        this.Emojis = Emojis;
    }

    @NonNull
    @Override
    public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmojiViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emoji_received_header, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiViewHolder holder, int position) {
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
