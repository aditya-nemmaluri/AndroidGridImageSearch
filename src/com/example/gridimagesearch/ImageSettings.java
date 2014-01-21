package com.example.gridimagesearch;

import java.io.Serializable;

public class ImageSettings implements Serializable {


	private static final long serialVersionUID = 8224624602655405548L;
	public  String filterText;
	public  String imgType;
	public  String imgColor;
	public  String imgSz;
	
	public ImageSettings() {
		filterText="";
		imgType="";
		imgColor="";
		imgSz="small";
	}
	
	public ImageSettings(String filter, String imgType, String imgColor, String imgSz) {
		filterText=filter;
		this.imgType=imgType;
		this.imgColor=imgColor;
		this.imgSz=imgSz;
	}
}
