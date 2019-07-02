import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


// TODO: Auto-generated Javadoc
/**
 * The Class Equalizer.
 */
public class Equalizer {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String args[]) throws IOException {
		//Get data from text file
		//Store each line
		List<String> list = getInput();
		for (int i=0; i<list.size(); i++) {
			System.out.println(list.get(i));
		}

		//Split lines and make matrix with longs
		//Record each variable
		Variable tempVar;
		List<Variable> varInd = new ArrayList<Variable>();
		long[][] s_mat = new long[list.size()][list.size()];
		
		//Make Mat into long with variables = 0
		//Index each variable
		for (int i=0; i<list.size(); i++) {
			String temp = list.get(i);
			String[] vals = temp.split(",");
			for (int j=0; j<vals.length; j++) {
				temp = vals[j];
				if (temp.charAt(0) == '!') {
					s_mat[i][j] = -1;
					tempVar = new Variable(i, j);				
					varInd.add(tempVar);
				} else {
					s_mat[i][j] = Long.parseLong(vals[j]);
				}
			}
		}
		
		//Number of Variables
		int n = varInd.size();
		System.out.println("\n\n" + n + "\n\n");
		
		//Find constant totals for each variable		
		int index = 0;
		long[] constant = new long[n];
		for (int i=0; i<list.size(); i++) {
			for (int j=0; j<list.size(); j++) {
				if (s_mat[i][j] == -1) {
					if(s_mat[i-1][j] != -1) varInd.get(index).setTotal(s_mat[i-1][j]);
					if(s_mat[i+1][j] != -1) varInd.get(index).setTotal(s_mat[i+1][j]);
					if(s_mat[i][j-1] != -1) varInd.get(index).setTotal(s_mat[i][j-1]);
					if(s_mat[i][j+1] != -1) varInd.get(index).setTotal(s_mat[i][j+1]);
					constant[index] = varInd.get(index).getTotal();
					index++;
				}
			}
		}
		prsingleMat(constant);
		//Create Matrix for solution	
		long[][] mat = createSMat(varInd, n);
		
		//Copy Org Matrix
		long[][] mutableMat = setArray(mat);

		//Find determinant
		long det = determinant(mutableMat);

		prdoubleMat(mat);
		prdoubleMat(s_mat);
		
		//solve for each variable
		long[] results = new long[n];
		for (int i=0; i<n; i++) {
			mutableMat = setArray(mat);
			for (int j=0; j<n; j++) {
				mutableMat[j][i] = constant[j];
			}
			//prdoubleMat(mutableMat);
			results[i] = determinant(mutableMat);
		}
		//prsingleMat(results);
		for (int i=0; i<n; i++) {
			System.out.println(asFraction(results[i],det));
		}
		System.out.println();
		
		//Fill Original Matrix with answers
		String[] output = new String[list.size()];
		String temp = "";
		index = 0;
		for (int i=0; i<list.size(); i++) {
			for (int j=0; j<list.size(); j++) {
				if (s_mat[i][j] == -1) {
					temp += asFraction(results[index],det) + ",";
					index++;
				} else if (j != list.size()-1) {
					temp += s_mat[i][j] + ",";
				} else {
					temp += s_mat[i][j];
				}
			}
			output[i] = temp;
			temp = "";
		}
		list = Arrays.asList(output);
		for (int i=0; i<list.size(); i++) {
			System.out.println(list.get(i));
		}
		
		
		Charset utf8 = StandardCharsets.UTF_8;
		try {
			Files.write(Paths.get("heights.txt"), list, utf8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the input.
	 *
	 * @return the input
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<String> getInput() throws IOException {
		List<String> list = new ArrayList<String>();
		ZipFile zipFile = new ZipFile("C:\\Users\\Randall_Thornton\\Downloads\\input.zip");
		ZipEntry zipEntry = zipFile.getEntry("ponds.txt");
		InputStream iStream = zipFile.getInputStream(zipEntry);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(iStream));
			String text = null;

			while ((text = br.readLine()) != null) {
				list.add(text);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				br.close();
			}
		}
		return list;
		
	}

	/**
	 * Creates the S mat.
	 *
	 * @param varInd the var ind
	 * @param n the n
	 * @return the long[][]
	 */
	public static long[][] createSMat(List<Variable> varInd, int n) {
		long[][] mat = new long[n][n];
		for (int i=0; i<n; i++) {
			//System.out.println(varInd.get(i).toString());
			for (int j=0; j<n; j++) {
				if(i==j) {
					mat[i][j] = 4;
				} else {
					if (varInd.get(i).getLoc_x() == varInd.get(j).getLoc_x() && varInd.get(i).getLoc_y() + 1 == varInd.get(j).getLoc_y()) {
						mat[i][j] = -1;
						mat[j][i] = -1;
					}
					if (varInd.get(i).getLoc_x() - 1 == varInd.get(j).getLoc_x() && varInd.get(i).getLoc_y() == varInd.get(j).getLoc_y()) {
						mat[i][j] = -1;
						mat[j][i] = -1;
					}
				}
			}
		}
		return mat;		
	}
		
	/**
	 * Determinant.
	 *
	 * @param a the a
	 * @return the long
	 */
	public static long determinant(long[][] a) {
		int n = a.length;
		long p;
		long d = 1;
		for (int i=0; i < n; i++) {
			p = d;
			d = a[i][i];
			for (int j=0; j<n; j++) {
				for (int k=0; k<n; k++) {
					if(i!=j) {
						if(i!=k) a[j][k] = ((d * a[j][k]) - (a[j][i] * a[i][k])) / p;
					}
				}
				if(i!=j) a[j][i] = 0;
			}
		}	
		return a[n-1][n-1];
	}

	/**
	 * Gcm.
	 *
	 * @param a the a
	 * @param b the b
	 * @return the long
	 */
	public static long gcm(long a, long b) {
		return b == 0 ? a : gcm(b, a % b);
	}

	/**
	 * As fraction.
	 *
	 * @param a the a
	 * @param b the b
	 * @return the string
	 */
	public static String asFraction(long a, long b) {
		long gcm = gcm(a, b);
		return (a / gcm) + "/" + (b / gcm);
	}

	/**
	 * Sets the array.
	 *
	 * @param mat the mat
	 * @return the long[][]
	 */
	public static long[][] setArray(long[][] mat) {
		long[][] old = new long[mat.length][mat.length];
		for(int i=0; i<mat.length; i++)
			for(int j=0; j<mat[i].length; j++)
				old[i][j]=mat[i][j];
		return old;
	}

	/**
	 * Prsingle mat.
	 *
	 * @param results the results
	 */
	public static void prsingleMat(long[] results) {
		int n = results.length;
		for (int i = 0; i < n; i++) {
			System.out.print(results[i] + "\n");
		}
		System.out.println();
	}

	/**
	 * Prdouble mat.
	 *
	 * @param mutableMat the mutable mat
	 */
	public static void prdoubleMat(long[][] mutableMat) {
		int n = mutableMat.length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.printf(" %10d ", mutableMat[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
}