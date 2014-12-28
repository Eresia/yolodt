
//Test de l'interface graphique
public class TestGUI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GraphInter gui = new GraphInter("yolodt");
		try{
			System.setErr(new CustomErrStream(gui.getErrorArea()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gui.setVisible(true);
		
	}

}
