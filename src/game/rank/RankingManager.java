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

	public static ArrayList<Integer> rank_list;

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

				while (input.available() >= 4)
					RankingManager.rank_list.add(input.readInt());

				input.close();

				RankingManager.rank_list.sort(new Comparator<Integer>() {
					@Override
					public int compare(Integer o1, Integer o2) {
						// TODO Auto-generated method stub
						return Integer.compare(o2, o1);
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

	public static void add_rank(int rank_score) {
		RankingManager.rank_list.add(rank_score);
		RankingManager.rank_list.sort(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				// TODO Auto-generated method stub
				return Integer.compare(o2, o1);
			}
		});

		try {
			RankingManager.output.writeInt(rank_score);
			RankingManager.output.flush();
		} catch (IOException e) {
		}
	}
}