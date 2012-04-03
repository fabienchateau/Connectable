package org.mt4jx.components.visibleComponents.widgets.pdf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

public class PathUtil {
	private static String separator = "/"; // used for string return values
	private static String[] toAbsolutePathStringArray(File f) {
		Vector<String> tmp = new Vector<String>();
		String absolutePath;
		try {
			absolutePath = f.getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
//		System.out.println(absolutePath);
//		System.out.println(f.getPath() + "<->" + absolutePath);
		StringTokenizer st = new StringTokenizer(absolutePath, "/\\", false);
		String current;
		while(st.hasMoreTokens() &&((current=st.nextToken())!=null)){
//			System.out.println("\ttoken:" + current);
			tmp.add(current);
		}
		String[] result = tmp.toArray(new String[tmp.size()]);
		return result;
	}
	public static String toAbsolutePathString(File f){
		String result = "";
		String[] path = toAbsolutePathStringArray(f);
		for (int i = 0; i < path.length; i++) {
			result+=path[i]+separator;
		}
		return result;
	}
	/**
	 * @param root
	 * @param candidate
	 * @return The part of the absolute path of the file which is the same for both files
	 */
	private static String[] getCommonPath(File root, File candidate){
		String[] rootPath = toAbsolutePathStringArray(root);
		String candidatePath = toAbsolutePathString(candidate);
		String current = "";
		Vector<String> result = new Vector<String>();

		for (int i = 0; i < rootPath.length; i++) {
			current += rootPath[i] + separator;
			if(candidatePath.startsWith(current)){
				result.add(rootPath[i]);
			}else{
				break;
			}
		}
		if(result.size()>0){ // hasCommonPath
			return result.toArray(new String[result.size()]);
		}else{
			throw new IllegalArgumentException("No common path: " + root + "<->" + candidate);
		}
	}
	private static String getCommonPathString(File root, File candidate){
		String result = "";
		String[] path = getCommonPath(root, candidate);
		for (int i = 0; i < path.length; i++) {
			result+=path[i]+separator;
		}
		return result;
	}
	public static String toRelativePathString(File root, File sub){
		String[] relativePath = toRelativePath(root, sub);
		String result = "";
//		System.out.println(sub + "-rel->" + root);

		if(relativePath.length==1){
			return separator + relativePath[0];
		}else{
			for (int i = 0; i < relativePath.length; i++) {
				result += separator + relativePath[i];
//				System.out.println("[REL"+i+"]" + result);
			}
			return result;
		}
	}
	public static boolean isSubFolder(File parent, File candidate){
		try {
			String sParent = parent.getCanonicalPath();
			String sCandidate = candidate.getCanonicalPath();
			sParent.replaceAll("\\/:", "");
			sCandidate.replaceAll("\\/:", "");
//			System.out.println("compare: " + sParent + "-sub>" + sCandidate);
			return sCandidate.startsWith(sParent);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static String[] toRelativePath(File rootFolder, File subFolder){
		File absRoot = rootFolder.getAbsoluteFile();
		if(!isSubFolder(absRoot, subFolder)){
			throw new IllegalArgumentException(subFolder + " is not under " + absRoot);
		}
		String[] commonPath = getCommonPath(absRoot, subFolder);
//		System.out.println("toRelativePath(root="+absRoot+", sub="+subFolder+")");
		// System.out.println("commonPath.length=" + commonPath.length);

		String[] subAbs = toAbsolutePathStringArray(subFolder);
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < subAbs.length; i++) {
			// System.out.println("subAbs["+i+"]: " + subAbs[i]);
//			System.out.println("[" + i + "]" + subAbs[i]);
			if(i>commonPath.length-1){
				result.add(subAbs[i]);
			}
		}
		return result.toArray(new String[result.size()]);
	}
	public static String toNormalizedAbsolutePath(File f){
		String[] abs = toAbsolutePathStringArray(f);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < abs.length; i++) {
			sb.append(abs[i]);
			if(i<abs.length-1){
				sb.append(separator);
			}
		}
		String normalized = new String(sb);
//		System.out.println(f.getPath() + " -norm-> " + normalized);
		return normalized;
	}
}
