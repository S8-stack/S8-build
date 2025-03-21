package com.s8.build.lists;

public class S8Modules {
	
	
	public static String[] listRepositories(String root) {
	
	
	return new String[] { 
			
			/* <core> */
			
			/* API */
			root + "/S8-api",
			
			/* I/O */
			root + "/S8-core-io-JSON",
			root + "/S8-core-io-xml",
			root + "/S8-core-io-csv",
			root + "/S8-core-io-bytes",
			
			/* BOHR */
			root + "/S8-core-bohr-atom",
			root + "/S8-core-bohr-beryllium",
			root + "/S8-core-bohr-lithium",
			root + "/S8-core-bohr-neodymium",
			root + "/S8-core-bohr-neon",
			
			/* ARCH */
			root + "/S8-core-arch-silicon",
			root + "/S8-core-arch-titanium",
			
			/* DB */
			root + "/S8-core-db-tellurium",
			root + "/S8-core-db-cobalt",
			root + "/S8-core-db-copper",
			
			/* WEB */
			root + "/S8-core-web-helium",
			root + "/S8-core-web-carbon",
			root + "/S8-core-web-manganese",
			root + "/S8-core-web-xenon",
			
			/* BUILD */
			root + "/S8-build",
			
			/* </core> */
			
			
			
			/* <pkgs> */
			root + "/S8-pkgs-ui-carbide",
			root + "/S8-pkgs-charts",
			root + "/S8-pkgs-io-HTML",
			root + "/S8-pkgs-io-SVG",
			root + "/S8-pkgs-io-WebGL",
			root + "/S8-pkgs-people",
			root + "/S8-pkgs-ui-paper",
			/* </pkgs> */
			
			/* <fwks> */
			root + "/S8-fwks-palm",
			/* </fwks> */
			
			
				
		};
	
	}
}
