/*
Copyleft (C) 2005 H�lio Perroni Filho
xperroni@yahoo.com
ICQ: 2490863

This file is part of ChatterBean.

ChatterBean is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

ChatterBean is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with ChatterBean (look at the Documents/ directory); if not, either write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA, or visit (http://www.gnu.org/licenses/gpl.txt).
 */

package bitoflife.chatterbean.aiml;

import hha.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import bitoflife.chatterbean.AliceBot;
import bitoflife.chatterbean.Context;
import bitoflife.chatterbean.Match;
import bitoflife.chatterbean.util.UserData;

public class Add extends TemplateElement {
	/*
	 * Attributes
	 */

	private String name;
	private String name_att;

	/*
	 * Constructors
	 */

	public Add(Attributes attributes) {
		name = attributes.getValue(0);
		name_att = attributes.getQName(0);
	}

	public Add(String name, Object... children) {
		super(children);

		this.name = name;
	}

	/*
	 * Methods
	 */

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		Add compared = (Add) obj;
		if (!name.equals(compared.name))
			return false;
		return super.equals(compared);
	}

	public String process(Match match) {
		String output = super.process(match);

		if (match == null)
			output = "<add name=\"" + name + "\">" + output + "</add>";
		else {
			AliceBot bot = match.getCallback();
			Context context = (bot != null ? bot.getContext() : null);
			String key = null;
			String data = null;
			if (context != null) {
				if ("name".equals(name_att)) {
					key = name;
					data = output;
				} else {
					key = name_att;
					data = name;
				}
				MainActivity.main.getBot().addUserData(key, data,
						new java.util.Date());
			}
		}
		// java.lang.System.out.println("Set:"+output);
		return output;
	}

	@Override
	public String toString() {
		StringBuilder value = new StringBuilder();
		value.append("<add name=\"" + name + "\">");
		for (TemplateElement i : children())
			value.append(i.toString());
		value.append("</add>");

		return value.toString();
	}

}
