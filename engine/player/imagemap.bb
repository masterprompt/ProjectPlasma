;============================
;HOTSPOT CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const HOTSPOT_MAX%=256

;============================
;GLOBALS
;============================
Global hotspot_ID.hotspot[HOTSPOT_MAX%];Primary Key Object Pointer
Global hotspot_Index%;Primary Key ID Management Counter

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type hotspot
	;Purpose: 
	;Properties:
	Field ID%;
	Field hotspottype%;
	Field coord%[3];
	Field target$;
	Field command$;
	Field state%;
End Type

;============================
;METHODS
;============================
Function hotspotStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
End Function

Function hotspotStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.hotspot=Each hotspot
		hotspotDelete(this)
	Next
End Function

Function hotspotNew.hotspot()
	;Purpose: CONSTRUCTOR - Creates hotspot Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: hotspot Instance
	this.hotspot=New hotspot
	this\ID%=0
	this\hotspottype%=0
	For hotspotloop=0 To 3
		this\coord%[hotspotloop]=0
	Next
	this\target$=""
	this\command$=""
	this\state%=0
	hotspot_Index=hotspot_Index+1
	this\ID%=hotspot_Index
	hotspot_ID[this\ID%]=this
	Return this
End Function

Function hotspotDelete(this.hotspot)
	;Purpose: DESTRUCTOR - Removes hotspot Instance
	;Parameters: hotspot Instance
	;Return: None
	this\command$=""
	this\target$=""
	For hotspotloop=0 To 3
		this\coord%[hotspotloop]=0
	Next
	Delete this
End Function

Function hotspotUpdate()
	;Purpose: Main Loop Update. Updates all hotspot Instances.
	;Parameters: None
	;Return: None
	For this.hotspot=Each hotspot
		;Do stuff to 'this' here!
		;Select this\state
		;	Case HOTSPOT_STATE_DOWN
		;		
		;End Select	
	Next
End Function

Function hotspotWrite(this.hotspot,hotspotfile)
	;Purpose: Writes hotspot Property Values to File
	;Parameters: hotspot Instance, hotspotfile=filehandle
	;Return: None
	WriteInt(hotspotfile,this\ID%)
	WriteInt(hotspotfile,this\hotspottype%)
	For hotspotloop=0 To 3
		WriteInt(hotspotfile,this\coord%[hotspotloop])
	Next
	WriteString(hotspotfile,this\target$)
	WriteString(hotspotfile,this\command$)
	WriteInt(hotspotfile,this\state%)
End Function

Function hotspotRead.hotspot(hotspotfile)
	;Purpose: Reads File To hotspot Property Values
	;Parameters: hotspotfile=filehandle
	;Return: hotspot Instance
	this.hotspot=New hotspot
	this\ID%=ReadInt(hotspotfile)
	this\hotspottype%=ReadInt(hotspotfile)
	For hotspotloop=0 To 3
		this\coord%[hotspotloop]=ReadInt(hotspotfile)
	Next
	this\target$=ReadString(hotspotfile)
	this\command$=ReadString(hotspotfile)
	this\state%=ReadInt(hotspotfile)
	hotspot_ID[this\ID%]=this
	Return this
End Function

Function hotspotSave(hotspotfilename$="hotspot.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: hotspotfilename$= Name of File
	;Return: None
	hotspotfile=WriteFile(hotspotfilename$)
	For this.hotspot= Each hotspot
		hotspotWrite(this,hotspotfile)
	Next
	CloseFile(hotspotfile)
End Function

Function hotspotOpen(hotspotfilename$="hotspot.dat")
	;Purpose: Opens a Object Property file
	;Parameters: hotspotfilename$=Name of Property File
	;Return: None
	hotspotfile=ReadFile(hotspotfilename$)
	Repeat
		hotspotRead(hotspotfile)
	Until Eof(hotspotfile)
	CloseFile(hotspotfile)
End Function

Function hotspotCopy.hotspot(this.hotspot)
	;Purpose: Creates a Copy of a hotspot Instance
	;Parameters: hotspot Instance
	;Return: Copy of hotspot Instance
	copy.hotspot=hotspotNew()
	copy\ID%=this\ID%
	copy\hotspottype%=this\hotspottype%
	For hotspotloop=0 To 3
		copy\coord%[hotspotloop]=this\coord%[hotspotloop]
	Next
	copy\target$=this\target$
	copy\command$=this\command$
	copy\state%=this\state%
	Return copy
End Function

Function hotspotMimic(this.hotspot,mimic.hotspot)
	;Purpose: Copies Property Values from one hotspot Instance To another
	;Parameters: hotspot Instance
	;Return: None
	mimic\ID%=this\ID%
	mimic\hotspottype%=this\hotspottype%
	For hotspotloop=0 To 3
		mimic\coord%[hotspotloop]=this\coord%[hotspotloop]
	Next
	mimic\target$=this\target$
	mimic\command$=this\command$
	mimic\state%=this\state%
End Function

Function hotspotCreate%(hotspotID%,hotspothotspottype%,coord0%,coord1%,coord2%,coord3%,hotspottarget$,hotspotcommand$,hotspotstate%)
	;Purpose: Creates an Object and returns it ID
	;Parameters: Object's Properties
	;Return: Object's ID%
	this.hotspot=hotspotNew()
	this\ID%=hotspotID%
	this\hotspottype%=hotspothotspottype%
	this\coord%[0]=coord0%
	this\coord%[1]=coord1%
	this\coord%[2]=coord2%
	this\coord%[3]=coord3%
	this\target$=hotspottarget$
	this\command$=hotspotcommand$
	this\state%=hotspotstate%
	Return this\ID
End Function

Function hotspotDestroy(hotspotid%)
	;Purpose: Remove object by ID
	;Parameters: hotspotid%=Object's ID
	;Return: None
	hotspotDelete(hotspot_ID[hotspotid%])
End Function

Function hotspotSet(this.hotspot,hotspotID%,hotspothotspottype%,coord0%,coord1%,coord2%,coord3%,hotspottarget$,hotspotcommand$,hotspotstate%)
	;Purpose: Set hotspot Property Values
	;Parameters: hotspot Instance and Properties
	;Return: None
	this\ID%=hotspotID%
	this\hotspottype%=hotspothotspottype%
	this\coord%[0]=coord0%
	this\coord%[1]=coord1%
	this\coord%[2]=coord2%
	this\coord%[3]=coord3%
	this\target$=hotspottarget$
	this\command$=hotspotcommand$
	this\state%=hotspotstate%
End Function

Function hotspotMethod.hotspot(this.hotspot)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function

;============================
;IMAGEMAP CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const IMAGEMAP_MAX%=32
Const IMAGEMAP_HOTSPOT_MAX=16
Const IMAGEMAP_STATE_NOTACTIVE=0
Const IMAGEMAP_STATE_ACTIVE=1

;============================
;GLOBALS
;============================
Global imagemap_ID.imagemap[IMAGEMAP_MAX%];Primary Key Object Pointer
Global imagemap_Index%;Primary Key ID Management Counter
Global imagemap_Active%

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type imagemap
	;Purpose:  Simple Image based GUI based on html imagemap; Store Image and HotSpots
	;Properties:
	Field ID%;
	Field audioID%;
	Field filename$;
	Field x#;
	Field y#;
	Field imgsrc$;
	Field image%;
	Field hotspots%;
	Field hotspot.hotspot[IMAGEMAP_HOTSPOT_MAX];
	Field state%;
	Field mousestate%
	Field mouseoldstate%
End Type

;============================
;METHODS
;============================
Function imagemapStart(imagemapfilename$="imagemap.dat")
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	imagemapOpen(imagemapfilename$)
	For this.imagemap = Each imagemap
		this\image%=LoadImage(this\imgsrc$)
		;hotspot offset correction
		For imagemaploop=1 To this\hotspots
			this\hotspot[imagemaploop]\coord[0]=this\hotspot[imagemaploop]\coord[0]+this\x
			this\hotspot[imagemaploop]\coord[1]=this\hotspot[imagemaploop]\coord[1]+this\y
			this\hotspot[imagemaploop]\coord[2]=this\hotspot[imagemaploop]\coord[2]+this\x
			this\hotspot[imagemaploop]\coord[3]=this\hotspot[imagemaploop]\coord[3]+this\y
		Next
	Next	
End Function

Function imagemapStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.imagemap=Each imagemap
		imagemapDelete(this)
	Next
End Function

Function imagemapNew.imagemap()
	;Purpose: CONSTRUCTOR - Creates imagemap Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: imagemap Instance
	this.imagemap=New imagemap
	this\ID%=0
	this\audioID%=0		
	this\filename$=""
	this\x#=0.0
	this\y#=0.0
	this\imgsrc$=""
	this\image%=0
	this\hotspots%=0
	For imagemaploop=1 To IMAGEMAP_HOTSPOT_MAX
		this\hotspot.hotspot[imagemaploop]=hotspotNew()
	Next
	this\state%=0
	imagemap_Index=imagemap_Index+1
	this\ID%=imagemap_Index
	imagemap_ID[this\ID%]=this
	Return this
End Function

Function imagemapDelete(this.imagemap)
	;Purpose: DESTRUCTOR - Removes imagemap Instance
	;Parameters: imagemap Instance
	;Return: None
	For imagemaploop=1 To IMAGEMAP_HOTSPOT_MAX
		hotspotDelete(this\hotspot[imagemaploop])
	Next
	FreeImage(this\image%)
	this\imgsrc$=""
	this\y#=0.0
	this\x#=0.0
	this\filename$=""
	Delete this
End Function

Function imagemapUpdate()
	;Purpose: Checks for mouse and hotspot events. called in main imagemaploop
	;Parameters: none
	;Return: none
	For this.imagemap=Each imagemap
		Select this\state
			Case IMAGEMAP_STATE_ACTIVE
				imagemap_Active=this\ID
				If this\image 
					Cls()
					DrawBlock(this\image%,this\x,this\y)
				EndIf	
				;check mouse and hotspot events
				For imagemaploop% = 1 To this\hotspots%
					hotspot.hotspot=this\hotspot[imagemaploop%]
					;idle
					If MouseX()>=hotspot\coord%[0] And MouseX()<hotspot\coord%[2] 
						If MouseY()>=hotspot\coord%[1] And MouseY()<hotspot\coord%[3]  
							this\mousestate=MouseDown(1)
							;hover
							If this\mousestate=0 And this\mouseoldstate=0
								;Color(127,127,127)
								hotspotshade=-32
								hotspottint=32
								hotspotred=223
								hotspotgreen=223
								hotspotblue=223
							;hit
							ElseIf this\mousestate=1 And this\mouseoldstate=0
								;Color(0,127,0)	
								hotspotshade=32
								hotspottint=-32								
								hotspotred=0
								hotspotgreen=223
								hotspotblue=0
							;hold
							ElseIf this\mousestate=1 And this\mouseoldstate=1
								;Color(255,255,255)
								hotspotshade=32
								hotspottint=-32								
								hotspotred=223
								hotspotgreen=223
								hotspotblue=223								
							;release
							ElseIf this\mousestate=0 And this\mouseoldstate=1
								;Color(0,255,0)
								hotspotred=0
								hotspotgreen=255
								hotspotblue=0								
								;temp
								Select hotspot\command$
									Case "play"
										game_State=GAME_STATE_PLAY
									Case "credits"
										this\state=IMAGEMAP_STATE_NOTACTIVE
										imagemap_ID[2]\state=IMAGEMAP_STATE_ACTIVE
									Case "exit" 
										game_State=GAME_STATE_EXIT
									Case "back"
										this\state=IMAGEMAP_STATE_NOTACTIVE
										imagemap_ID[1]\state=IMAGEMAP_STATE_ACTIVE
								End Select							
							EndIf							
							Exit
						EndIf
					EndIf	
				Next
				this\mouseoldstate=this\mousestate
				this\mousestate=0
				Color(hotspotred+hotspottint,hotspotgreen+hotspottint,hotspotblue+hotspottint)
				Line(hotspot\coord%[0],hotspot\coord%[1],hotspot\coord%[2],hotspot\coord%[1])
				Line(hotspot\coord%[0],hotspot\coord%[3],hotspot\coord%[0],hotspot\coord%[1])
				Color(hotspotred+hotspotshade,hotspotgreen+hotspotshade,hotspotblue+hotspotshade)
				Line(hotspot\coord%[2],hotspot\coord%[1],hotspot\coord%[2],hotspot\coord%[3])
				Line(hotspot\coord%[2],hotspot\coord%[3],hotspot\coord%[0],hotspot\coord%[3])
				;Rect(hotspot\coord%[0],hotspot\coord%[1],hotspot\coord%[2]-hotspot\coord%[0],hotspot\coord%[3]-hotspot\coord%[1],0)
				Exit
		End Select
	Next
End Function

Function imagemapWrite(this.imagemap,imagemapfile)
	;Purpose: Writes imagemap Property Values to File
	;Parameters: imagemap Instance, imagemapfile=filehandle
	;Return: None
	WriteInt(imagemapfile,this\ID%)
	WriteInt(imagemapfile,this\audioID%)	
	WriteString(imagemapfile,this\filename$)
	WriteFloat(imagemapfile,this\x#)
	WriteFloat(imagemapfile,this\y#)
	WriteString(imagemapfile,this\imgsrc$)
	WriteInt(imagemapfile,this\image%)
	WriteInt(imagemapfile,this\hotspots%)
	For imagemaploop=1 To IMAGEMAP_HOTSPOT_MAX
		hotspotWrite(this\hotspot.hotspot[imagemaploop],imagemapfile)
	Next
	WriteInt(imagemapfile,this\state%)
End Function

Function imagemapRead.imagemap(imagemapfile)
	;Purpose: Reads File To imagemap Property Values
	;Parameters: imagemapfile=filehandle
	;Return: imagemap Instance
	this.imagemap=New imagemap
	this\ID%=ReadInt(imagemapfile)
	this\audioID%=ReadInt(imagemapfile)	
	this\filename$=ReadString(imagemapfile)
	this\x#=ReadFloat(imagemapfile)
	this\y#=ReadFloat(imagemapfile)
	this\imgsrc$=ReadString(imagemapfile)
	this\image%=ReadInt(imagemapfile)
	this\hotspots%=ReadInt(imagemapfile)
	For imagemaploop=1 To IMAGEMAP_HOTSPOT_MAX
		this\hotspot.hotspot[imagemaploop]=hotspotRead(imagemapfile)
	Next
	this\state%=ReadInt(imagemapfile)
	imagemap_ID[this\ID%]=this
	Return this
End Function

Function imagemapSave(imagemapfilename$="imagemap.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: imagemapfilename$= Name of File
	;Return: None
	imagemapfile=WriteFile(imagemapfilename$)
	For this.imagemap= Each imagemap
		imagemapWrite(this,imagemapfile)
	Next
	CloseFile(imagemapfile)
End Function

Function imagemapOpen(imagemapfilename$="imagemap.dat")
	;Purpose: Opens a Object Property file
	;Parameters: imagemapfilename$=Name of Property File
	;Return: None
	imagemapfile=ReadFile(imagemapfilename$)
	Repeat
		imagemapRead(imagemapfile)
	Until Eof(imagemapfile)
	CloseFile(imagemapfile)
End Function

Function imagemapCopy.imagemap(this.imagemap)
	;Purpose: Creates a Copy of a imagemap Instance
	;Parameters: imagemap Instance
	;Return: Copy of imagemap Instance
	copy.imagemap=imagemapNew()
	copy\audioID%=this\audioID%	
	copy\filename$=this\filename$
	copy\x#=this\x#
	copy\y#=this\y#
	copy\imgsrc$=this\imgsrc$
	copy\image%=this\image%
	copy\hotspots%=this\hotspots%
	For imagemaploop=1 To IMAGEMAP_HOTSPOT_MAX
		copy\hotspot.hotspot[imagemaploop]=hotspotCopy(this\hotspot[imagemaploop])
	Next
	copy\state%=this\state%
	Return copy
End Function

Function imagemapMimic(this.imagemap,mimic.imagemap)
	;Purpose: Copies Property Values from one imagemap Instance To another
	;Parameters: imagemap Instance
	;Return: None
	mimic\filename$=this\filename$
	mimic\audioID%=this\audioID%	
	mimic\x#=this\x#
	mimic\y#=this\y#
	mimic\imgsrc$=this\imgsrc$
	mimic\image%=this\image%
	mimic\hotspots%=this\hotspots%
	For imagemaploop=1 To IMAGEMAP_HOTSPOT_MAX
		hotspotMimic(this\hotspot[imagemaploop],mimic\hotspot[imagemaploop])
	Next
	mimic\state%=this\state%
End Function

Function imagemapCreate(imagemapfilename$,imagemapsrcname$="")
	;Purpose: Parses html client side imagemap file.
	;Parameters:
	;	imagemapname$ - name of imagemap. Can be used for reference purposes.
	;	imagemapfilename$ - html map file name, *.htm extension not required.
	;	imagemapsrcname$ - image file name. Valid map format extension required.	
	;Return:
	imagemapfile%=OpenFile(imagemapfilename+".htm") ;*.map
	If Not imagemapfile% Return
	While Not Eof(imagemapfile%)
		imagemapfileline$=Lower(ReadLine(imagemapfile%))
		imagemapfilelinelength%=Len(imagemapfileline$)
		For imagemaploop% = 1 To imagemapfilelinelength%
			imagemapchar$=Mid$(imagemapfileline$,imagemaploop%,1)
			Select imagemapchar$
				Case " ","<",">","=",Chr(34),Chr(39)
					;ignore whitespace and tags	
					imagemapword$=nil$			
				Default
					imagemapword$=imagemapword$+imagemapchar$
			End Select
			Select Lower(imagemapword$)
				Case "name"
					this.imagemap=imagemapNew() 
					this\imgsrc$=imagemapsrcname$					
					this\filename$=imagemapfilename$
				Case "area" 
					this\hotspots%=this\hotspots%+1
				Case "coords" ;coords define shape, rect by by limitation
					For imagemaploop% = imagemaploop%+1 To imagemapfilelinelength%
						imagemapchar$=Mid$(imagemapfileline$,imagemaploop%,1)
						Select imagemapchar$
							Case "="," "
								hotspotcoord%=0
								imagemapword$=nil$		
							Case ","
								this\hotspot[this\hotspots%]\coord[hotspotcoord%]=imagemapword$
								hotspotcoord%=hotspotcoord%+1
								imagemapword$=nil$
							Case Chr(34),Chr(39);quotes
								If hotspotcoord%>0 Or imagemapword$<>nil$
									this\hotspot[this\hotspots%]\coord[hotspotcoord%]=imagemapword$								
									Exit
								EndIf	
							Default
								imagemapword$=imagemapword$+imagemapchar$	
						End Select
					Next
				Case "alt" ;command$
					For imagemaploop% = imagemaploop%+1 To imagemapfilelinelength%
						imagemapchar$=Mid$(imagemapfileline$,imagemaploop%,1)
						Select imagemapchar$
							Case "="," "
								imagemapword$=nil$
							Case Chr(34),Chr(39);quotes
								If imagemapword$<>nil$
									this\hotspot[this\hotspots%]\command$=imagemapword$
									Exit
								EndIf	
							Default 
								imagemapword$=imagemapword$+imagemapchar$	
						End Select
					Next
				Case "href" 
					For imagemaploop% = imagemaploop%+1 To imagemapfilelinelength%
						imagemapchar$=Mid$(imagemapfileline$,imagemaploop%,1)
						Select imagemapchar$
							Case "="," "
								imagemapword$=nil$
							Case Chr(34),Chr(39);quotes
								If imagemapword$<>nil$
									this\hotspot[this\hotspots%]\target$=imagemapword$
									Exit
								EndIf	
							Default 
								imagemapword$=imagemapword$+imagemapchar$	
						End Select
				Next
			End Select
		Next	
	Wend
	CloseFile(imagemapfile%)
	Return this\ID%
End Function

Function imagemapDestroy(this.imagemap)
	;Purpose: Remove object by ID
	;Parameters: imagemapid%=Object's ID
	;Return: None
	If this<>Null
		For imagemaploop = 1 To hotspots%
			If this\hotspot[imagemaploop%]<>Null
				Delete this\hotspot[imagemaploop%] 
			EndIf
		Next
		If this\image% FreeImage(this\image)
		Delete this
	EndIf
End Function

Function imagemapDefaultDatWrite()
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD	
	this.imagemap=imagemap_ID[imagemapCreate("..\..\game\data\imagemap\start","..\..\game\data\imagemap\start.jpg")]
	this\imgsrc$="..\game\data\imagemap\start.jpg"
	this\audioID%=7
	this\x=60
	this\y=110
	this\state=IMAGEMAP_STATE_ACTIVE
	
	this.imagemap=imagemap_ID[imagemapCreate("..\..\game\data\imagemap\credits","..\..\game\data\imagemap\credits.jpg")]
	this\imgsrc$="..\game\data\imagemap\credits.jpg"
	this\x=130
	this\y=120
	imagemapSave()
End Function

Function imagemapMethod.imagemap(this.imagemap)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function

;imagemapDefaultDatWrite