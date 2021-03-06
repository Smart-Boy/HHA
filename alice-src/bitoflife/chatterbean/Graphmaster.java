/*
Copyleft (C) 2005 H閘io Perroni Filho
xperroni@yahoo.com
ICQ: 2490863

This file is part of ChatterBean.

ChatterBean is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

ChatterBean is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with ChatterBean (look at the Documents/ directory); if not, either write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA, or visit (http://www.gnu.org/licenses/gpl.txt).
 */

package bitoflife.chatterbean;

import hha.main.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import bitoflife.chatterbean.aiml.Category;

public class Graphmaster {
	/*
	 * Attributes
	 */

	/* The children of this node. */
	private final Map<String, Graphmaster> children = new HashMap<String, Graphmaster>();

	private int size = 0;
	private Graphmaster parent;
	private Category category;
	private String name; // The name of a node is the pattern element it
							// represents.

	private Graphmaster(String name) {
		this.name = name;
	}

	/**
	 * Constructs a new root node.
	 */
	public Graphmaster() {
	}

	/**
	 * Constructs a new tree, with this node as the root.
	 */
	public Graphmaster(List<Category> categories) {
		append(categories);
	}

	/*
	 * Methods
	 */

	private void append(Category category, String[] elements, int index) {
		Graphmaster child = children.get(elements[index]);
		if (child == null)
			appendChild(child = new Graphmaster(elements[index]));

		int nextIndex = index + 1;
		if (elements.length <= nextIndex)
			child.category = category;
		else
			child.append(category, elements, nextIndex);
	}

	private void appendChild(Graphmaster child) {
		// System.out.println(child.name);
//		if (isSharp(child.name)) {
//			children.put("#", child);
//		} else if (isDollar(child.name)) {
//			children.put("$", child);
//		} else {
			children.put(child.name, child);
//		}

		child.parent = this;
	}

	/**
	 * <p>
	 * Returns an array with three child nodes, in the following order:
	 * </p>
	 * <ul>
	 * <li>The "_" node;</li>
	 * <li>The node with the given name;</li>
	 * <li>The "*" node.</li>
	 * </ul>
	 * <p>
	 * If any of these nodes can not be found among this node's children, its
	 * position is filled by <code>null</code>.
	 * </p>
	 */
	private Graphmaster[] children(String name) {
		
		List<Graphmaster> graphs = new ArrayList<Graphmaster>();
		graphs.add(children.get("_"));
		graphs.add(children.get(name));
		for (String s : children.keySet())
		{
			if (isSharp(s))
			{
				graphs.add(children.get(s));
			}
		}
		graphs.add(children.get("%"));
		graphs.add(children.get("*")); 
		return graphs.toArray(new Graphmaster[graphs.size()]);
	}

	private boolean isWildcard() {
		return ("_".equals(name) || "*".equals(name));
	}

	private boolean isSharp() {
		// System.out.println(name);
		return ("#".equals(name.substring(0, 1)));
	}

	private boolean isPercent() {
		// System.out.println(name);
		return ("%".equals(name));
	}

	private boolean isDollar(String name2) {
		return ("$".equals(name2.substring(0, 1)));
	}

	private boolean isSharp(String name2) {
		// TODO Auto-generated method stub
		return ("#".equals(name2.substring(0, 1)));
	}

	java.util.regex.Pattern pattern = null;

	private Category matchSharpbf(Match match, int index) {
		// TODO Auto-generated method stub
		// System.out.println("Sharp:"+name);

		if (pattern == null)
			pattern = java.util.regex.Pattern.compile(name.substring(1),
					java.util.regex.Pattern.CASE_INSENSITIVE);

		StringBuffer str = new StringBuffer();
		boolean canMatch = false;
		boolean wontbefind = false;
		int n = match.getMatchPathLength();
		Matcher m;
		int BeginMatch = -1;
		int EndMatch = -1;
		for (int i = index; i < n; ++i) {
			str.append(match.getMatchPath(i));
			m = pattern.matcher(str.toString());
			System.out.println("Match:" + str.toString());
			if (m.matches()) {
				System.out.println("MatchSucceed");
				BeginMatch = i;
				canMatch = true;
			} else {

				System.out.println("MatchFailed");
				if (BeginMatch != -1) {
					EndMatch = i;
					break;
				}
				if (!m.hitEnd()) {
					System.out.println("Can't find Match!");
					wontbefind = true;
					break;
				}
			}
		}

		if (!canMatch) {
			// System.out.println("Matcher:"+match.getMatchPath(index));
			return null;
		} else {
			if (EndMatch == -1 || wontbefind)
				return null;

			System.out.println(BeginMatch + " " + EndMatch);

			int nextIndex = EndMatch;
			if (match.getMatchPathLength() <= nextIndex)
				return category;

			return matchChildren(match, nextIndex);
		}

	}

	private Category matchSharp(Match match, int index) {
		// TODO Auto-generated method stub
		// System.out.println("Sharp:"+name);

		if (pattern == null)
			pattern = java.util.regex.Pattern.compile(name.substring(1),
					java.util.regex.Pattern.CASE_INSENSITIVE);

		StringBuffer str = new StringBuffer();
		boolean canMatch = false;
		int n = match.getMatchPathLength();
		Matcher m;
		int BeginMatch = index;
		int EndMatch = -1;
		for (int i = index; i < n; ++i) {
			str.append(match.getMatchPath(i));
			m = pattern.matcher(str.toString());
//			System.out.println("Match:" + name + " <- " + str.toString());
			// System.out.println("Match:" + str.toString());
			if (m.matches()) {
				// System.out.println("MatchSucceed");
//				MainActivity.main.ShowTextOnUIThread("MatchSucceed:"
//						+ str.toString());
				EndMatch = i + 1;
				canMatch = true;

			} else {

				if (!m.hitEnd()) {
					// System.out.println("Can't find Match!");
					m = pattern.matcher("");
					if (m.matches())
					{
						EndMatch = index;
						canMatch = true;
					}
					break;
				}
			}
		}

		Category c = null;

		if (!canMatch) {
			// System.out.println("can't match!");
			return null;
		} else {

			// System.out.println(BeginMatch + " " + EndMatch);

			int nextIndex = EndMatch;
			if (match.getMatchPathLength() <= nextIndex)
				c = category;

			c = matchChildren(match, nextIndex);
		}

		if (c != null) {
			match.appendRegex(BeginMatch, EndMatch);
		}
		return c;
	}

	private Category match(Match match, int index) {
		// System.out.println(name);
		// StringBuffer sBuffer = new StringBuffer();
		if (isSharp())
			return matchSharp(match, index);

		if (isDollar(name))
			return matchDollar(match, index);

		if (isPercent())
			return matchPercent(match, index);

		if (isWildcard())
			return matchWildcard(match, index);

		if (!name.equals(match.getMatchPath(index)))
			return null;

		int nextIndex = index + 1;
		if (match.getMatchPathLength() <= nextIndex)
			return category;

		return matchChildren(match, nextIndex);
	}

	private Category matchPercent(Match match, int index) {
		// TODO Auto-generated method stub

		Category category = matchChildren(match, index);

		return category;
	}

	private Category matchDollar(Match match, int index) {
		// TODO Auto-generated method stub
		Context context = match.getCallback().getContext();
		Object o = context.property("var." + name.substring(1));
		// System.out.println("var." + name.substring(1));
		if (o instanceof java.util.regex.Pattern) {
			pattern = (java.util.regex.Pattern) o;
			// System.out.println("right");
			return matchSharp(match, index);
		}
		if (o instanceof String) {
			if (!o.equals(match.getMatchPath(index)))
				return null;

			int nextIndex = index + 1;
			if (match.getMatchPathLength() <= nextIndex)
				return category;

			return matchChildren(match, nextIndex);
		}
		return null;
	}

	private Category matchChildren(Match match, int nextIndex) {
		// System.out.print(match.getMatchPath(nextIndex)+"\n");
		Graphmaster[] nodes = children(match.getMatchPath(nextIndex));
		for (int i = 0, n = nodes.length; i < n; i++) {
			Category category = (nodes[i] != null ? nodes[i].match(match,
					nextIndex) : null);

			if (category != null)
				return category;
		}

		return null;
	}

	private Category matchWildcard(Match match, int index) {
		// System.out.print("遇到通配符!\n");

		int n = match.getMatchPathLength();
		for (int i = index; i < n; i++) {
			Category category = matchChildren(match, i);
			if (category != null) {
				match.appendWildcard(index, i);
				return category;
			}
		}

		if (category != null)
			match.appendWildcard(index, n);
		return category;
	}

	public void append(List<Category> categories) {
		for (Category category : categories)
			append(category);
	}

	public void append(Category category) {
		String matchPath[] = category.getMatchPath();
		append(category, matchPath, 0);
		size++;
	}

	/**
	 * Returns the Catgeory which Pattern matches the given Sentence, or
	 * <code>null</code> if it cannot be found.
	 */
	public Category match(Match match) {
		return matchChildren(match, 0);
	}

	public int size() {
		return size;
	}
}
