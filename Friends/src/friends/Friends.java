package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;
import java.util.*;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	private static Person search(Graph graph, String name) {
		if(!graph.map.containsKey(name)) return null;
		int value = graph.map.get(name).intValue();
		return graph.members[value];
	}
	
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		if(p1.equals(p2)) {
			return null;
		}
		
		Map<Person, Person> map = new HashMap<>();
		Queue <Person> queue = new Queue<>();
		Person first = search(g, p2);
		if(first == null) {
			return null;
		}
		
		queue.enqueue(first);
		map.put(first, null);
		while(!queue.isEmpty()) {
			Person person = queue.dequeue();
			Friend friend = person.first;
			while(friend != null) {
				Person newPerson = g.members[friend.fnum];
				if(!map.containsKey(newPerson)) {
					map.put(newPerson, person);
					queue.enqueue(newPerson);
					if(newPerson.name.contentEquals(p1)) {
						ArrayList<String> list = new ArrayList <>();
						Person current = newPerson;
						while (current != null) {
							list.add(current.name);
							current = map.get(current);
						}
						return list;
					}
				}
				friend = friend.next;
			}
		}
		return null;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/
		ArrayList<ArrayList<String>> listInList = new ArrayList<>();
		Set<Person> set = new HashSet<>();
		for(Person person : g.members) {
			if(person.school != null && person.school.contentEquals(school) && !set.contains(person)) {
				ArrayList<String> list = new ArrayList<>();
				list.add(person.name);
				set.add(person);
				Queue<Person> queue = new Queue<>();
				queue.enqueue(person);
				while(!queue.isEmpty()) {
					Person current = queue.dequeue();
					Friend friend = current.first;
					while(friend != null) {
						Person p = g.members[friend.fnum];
						if(p.school != null && p.school.contentEquals(school) && !set.contains(p)) {
							set.add(p);
							queue.enqueue(p);
							list.add(p.name);
						}
						friend = friend.next;
					}
				}
				listInList.add(list);
			}
		}
		return listInList;
		
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		Set<Person> set = new HashSet<>();
        ArrayList<String> list = new ArrayList<>();
        Map<Person, Friend> friendMap = new HashMap<>();
        for(Person person: g.members) {
            friendMap.put(person, person.first);
        }
 
        for(Person person: g.members) {
            if(!set.contains(person)) {
                Map<Person, Integer> dfs = new HashMap<>(), back = new HashMap<>();
                Stack<Person> stack = new Stack<>();
                stack.push(null);
                int dfsnum = 1;
                Person current = person;
 
                while(!stack.isEmpty()) {
                    if(current == null) break;
                    if(!set.contains(current)) {
                        dfs.put(current, dfsnum);
                        back.put(current, dfsnum++);
                        set.add(current);
                        stack.push(current);
                    }
                    while(friendMap.get(current) != null) {
                        Friend friend = friendMap.get(current);
                        Person nextPerson = g.members[friend.fnum];
                        if(set.contains(nextPerson)) {
                            back.put(current, Math.min(back.get(current), dfs.get(nextPerson)));
                            friendMap.put(current, friend.next);
                        }
                        else {
                            current = nextPerson;
                            break;
                        }
                    }
                    if(friendMap.get(current) == null) {
                        Person other = current;
                        current = stack.pop();
                        if(other == current) {
                            current = stack.peek();
                        }
                        if(current == null) break;
                        if(other != current) {
                            if(dfs.get(current) > back.get(other)) {
                                back.put(current, Math.min(back.get(current), back.get(other)));
                            }
                            else {
                                if(current == person) {
                                    if(dfs.get(other) >= back.get(other) + 2) {
                                        list.add(current.name);
                                    }
                                }
                                else {
                                    if(!list.contains(current.name))list.add(current.name);
                                }
                            }
                            friendMap.put(current, friendMap.get(current).next);
                        }
                    }
                }
            }
        }
        return list;		
	}
}

