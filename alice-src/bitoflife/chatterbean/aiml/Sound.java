/*
Copyleft (C) 2005 H锟絣io Perroni Filho
xperroni@yahoo.com
ICQ: 2490863

This file is part of ChatterBean.

ChatterBean is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

ChatterBean is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with ChatterBean (look at the Documents/ directory); if not, either write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA, or visit (http://www.gnu.org/licenses/gpl.txt).
 */

package bitoflife.chatterbean.aiml;

import hha.main.MainActivity;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

import bitoflife.chatterbean.AliceBot;
import bitoflife.chatterbean.Match;

public class Sound extends TemplateElement {
	/*
	 * Attributes
	 */
	String name;
	String type;

	// 类型可以是 loop 循环播放，pre 前置 ，suf 后置 ， back 背景 中的一种或几种，中间用“|”隔开
	// rate表示速率 后面的数值是百分比  loop后面也可以接数值，表示循环次数，不写次数是255次
	// 类型可以不写，默认是pre|back|rate100

	/*
	 * Constructors
	 */

	public Sound(Attributes attributes) {
		name = attributes.getValue("name");
		type = attributes.getValue("type");
		if (type == null)
			type = "pre|back|rate";
	}

	public Sound(String name, Object... children) {
		super(children);
	}

	/*
	 * Methods
	 */
	String[] atts = new String[] { "loop", "pre", "suf", "back", "rate" };

	public Map<String, Integer> getType(String type, String[] att) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String item : att) {
			map.put(item, 0);
		}
		map.put("rate", 100);
		String[] att_true = type.split("\\|");
		for (String item : att_true) {
			if (item.length()>=4 && item.substring(0, 4).equals("loop")) {
				if ((item.length() > 4))
					map.put("loop", Integer.parseInt(item.substring(4))-1);
				else
					map.put("loop", -1);
			} else if (item.length()>=4 && item.substring(0, 4).equals("rate")) {
				if ((item.length() > 4))
					map.put("rate", Integer.parseInt(item.substring(4)));
			} else
				map.put(item, 1);
		}
		return map;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		Sound compared = (Sound) obj;
		return super.equals(compared);
	}

	public String process(Match match) {
		String output = super.process(match);

		if (match == null)
			output = "<sound>" + output + "</sound>";
		else {
			java.lang.System.out.println("Sound:"+name);
			java.lang.System.out.println("Type:"+type);
			AliceBot bot = match.getCallback();
			Map<String, Integer> map = getType(type, atts);
			MainActivity.main.getPlayer().Add(name, map);
		}
		// java.lang.System.out.println("Set:"+output);
		return output;
	}
}
