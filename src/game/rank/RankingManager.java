package game.rank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class RankingManager {

	public static ArrayList<RankData> rank_list;

	private static DataOutputStream output;

	public static void initRanking() {
		RankingManager.rank_list = new ArrayList<>();

		File rank_folder = new File("res/rank");
		File rank_file = new File(rank_folder, "rank.dat");

		if (!rank_folder.exists()) {
			rank_folder.mkdirs();

			try {
				rank_file.createNewFile();
			} catch (IOException e) {
			}
		} else {
			try {

				DataInputStream input = new DataInputStream(new FileInputStream(rank_file));

				while (input.available() >= 1) {
					RankData sData = new RankData();
					sData.play_date = input.readUTF();
					sData.player_name = input.readUTF();
					sData.game_score = input.readInt();
					
					RankingManager.rank_list.add(sData);
				}

				input.close();

				RankingManager.rank_list.sort(new Comparator<RankData>() {
					@Override
					public int compare(RankData arg0, RankData arg1) {
						// TODO Auto-generated method stub
						int comp = Integer.compare(arg1.game_score, arg0.game_score);
						
						if(comp != 0)
							return comp;
						
						comp = arg1.play_date.compareTo(arg0.play_date);
						
						if(comp != 0)
							return comp;
						
						return arg0.player_name.compareTo(arg1.player_name);
					}
				});

			} catch (Exception e) {
			}
		}

		try {
			RankingManager.output = new DataOutputStream(new FileOutputStream(rank_file, true));
		} catch (FileNotFoundException e) {
		}
	}

	public static void add_rank(RankData rank_data) {
		RankingManager.rank_list.add(rank_data);
		RankingManager.rank_list.sort(new Comparator<RankData>() {
			@Override
			public int compare(RankData arg0, RankData arg1) {
				// TODO Auto-generated method stub
				int comp = Integer.compare(arg1.game_score, arg0.game_score);
				
				if(comp != 0)
					return comp;
				
				comp = arg1.play_date.compareTo(arg0.play_date);
				
				if(comp != 0)
					return comp;
				
				return arg0.player_name.compareTo(arg1.player_name);
			}
		});
		
		try {
			RankingManager.output.writeUTF(rank_data.play_date);
			RankingManager.output.writeUTF(rank_data.player_name);
			RankingManager.output.writeInt(rank_data.game_score);
			RankingManager.output.flush();
		} catch (IOException e) {
		}
	}
}