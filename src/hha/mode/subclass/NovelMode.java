package hha.mode.subclass;

import com.lilele.automatic.AuTomatic;

import hha.main.MainActivity;
import hha.mode.Mode;

public class NovelMode extends Mode{
 
	public NovelMode(MainActivity mainActivity, AuTomatic auto) {
		super(mainActivity, auto);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getModeName() {
		// TODO Auto-generated method stub
		return "novel";
	}

	@Override
	public void Run(int UserCount, int RobotCount) {
		// TODO Auto-generated method stub
		
	}

}
