
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;


public class Music_Recomm_System implements ActionListener
{

	private static String input ="data/music.csv";
	private static final int neighborhood_size=5;
	private static DataModel model = null;
	private  static LogLikelihoodSimilarity similarity = null;
	private static UserNeighborhood neighbor = null;
	private static UserBasedRecommender recommender = null;
	
	private static String[] music = {"Saibo", "Kabira", "Samjhawan", "Bolna", "Ishq Hai", "Ranjhan",
				 "Mere Bina", "Tera Chehra","Chaudhary", "Jhol", "Sahiba","Chor Bazaari", "Saadi Galli Aaja", 
				 			"Tujhe Dekha Toh", "Bachna Ae Haseeno", "Chaiyya Chaiyya", "Chor",
										 "kaala Chashma", "Prem ki naiyya", "Apna Bana Le"};


	private static String[] musicURL = {"thumbnails\\Saibo.jpg", "thumbnails\\Kabira.jpg", "thumbnails\\Samjhawan.jpg", "thumbnails\\Bolna.jpg",
										 "thumbnails\\Ishq Hai.jpg", "thumbnails\\Ranjhan.jpg",
										 "thumbnails\\Mere Bina.png", "thumbnails\\Tera Chehra.jpg",
										 "thumbnails\\Chaudhary.jpg", "thumbnails\\Jhol.jpg", "thumbnails\\Sahiba.jpg","thumbnails\\Chor Bazaari.jpg", "thumbnails\\Saadi Galli Aaja.jpg", 
				 						"thumbnails\\Tujhe Dekha Toh.jpg", "thumbnails\\Bachna Ae Haseeno.jpg", "thumbnails\\Chaiyya Chaiyya.jpg", "thumbnails\\Chor.jpg",
										 "thumbnails\\kaala Chashma.jpg", "thumbnails\\Prem ki naiyya.jpg", "thumbnails\\Apna Bana Le.jpg"};

	JFrame f;
	JTextField t1;
	JButton b1;
	JComboBox cb;
	public Music_Recomm_System()
	{
		f = new JFrame("Music Recommander");
		f.getContentPane().setForeground(new Color(1, 1,1));
		
		JLabel l1 = new JLabel("Music Recommendation System");
		l1.setFont(new Font("Cosmic sans ms",Font.BOLD,30));
		l1.setBounds(50,30,500,50);
		l1.setForeground(Color.BLACK);
		f.add(l1);

		JLabel l2 = new JLabel("Select a music you like");
		l2.setFont(new Font("Cosmic sans ms",Font.PLAIN,15));
		l2.setBounds(50,90,200,20);
		l2.setForeground(Color.BLACK);
		f.add(l2);

		cb = new JComboBox(music);
		cb.setBounds(50,120,500,30);
		f.add(cb);

		b1  = new JButton("Recommend");
		b1.setBackground(Color.white);
		b1.setForeground(Color.RED);
		b1.setBounds(50,160,150,30);
		b1.addActionListener(this);
		f.add(b1);

		f.setSize(950,500);
		f.setResizable(false);
		f.setLayout(null);
		f.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent  e)
	{
		try
		{
			
			f.revalidate();
			int selectedIndx = cb.getSelectedIndex();
			JLabel label = new JLabel("Recommanded Songs are......");
			label.setForeground(Color.DARK_GRAY);
			label.setFont(new Font("Arial", Font.ITALIC,15));
			label.setBounds(50,200,200,20);
			f.add(label);
			
			List<RecommendedItem> recommendations = recommender.recommend(selectedIndx+1, 5);
		
			System.out.println("Recommended Songs are:"+"");
			System.out.println("****************************************************************"+"");
			System.out.println("Movie_id\ttitle\t\testimated preferences"+"");
			int x=50,y=230;
			for(RecommendedItem recommendation : recommendations)
			{
				int musicId = (int) recommendation.getItemID();
				float estimatedPref = recommender.estimatePreference(selectedIndx+1,musicId);
				System.out.println(musicId + " "+music[musicId -1]+"\t" + estimatedPref);

				JLabel l1 = new JLabel(music[musicId -1]);
				l1.setBounds(x,y,150,20);
				f.add(l1);

				ImageIcon img = new ImageIcon(musicURL[musicId -1]);
				Image resized_img = img.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
				ImageIcon r_img = new ImageIcon(resized_img);
				JLabel img_sm=new JLabel(r_img);
				img_sm.setBounds(x,y+20,150,150);
				f.add(img_sm);

				x+=170;
				if(x>900)
				{
					x=50;
					y+=390;	
				}

			}
			f.repaint();
			f.revalidate();
			System.out.println("****************************************************************"+"");

		}
		catch(TasteException ex)
		{
			ex.printStackTrace();
		}
	} 

	public static void main(String[] args) throws IOException,TasteException 
	{
		
		model = new FileDataModel(new File(input));
		similarity = new LogLikelihoodSimilarity(model);
		neighbor = new NearestNUserNeighborhood(neighborhood_size, similarity,model);
		recommender = new GenericUserBasedRecommender(model, neighbor, similarity);
		
		new Music_Recomm_System();

		
	}

}
