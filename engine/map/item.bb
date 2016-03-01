;============================
;ITEM CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const ITEM_MAX%=32
Const ITEM_TYPE_SPAWNER=1

;============================
;GLOBALS
;============================
Global item_ID.item[ITEM_MAX%];Primary Key Object Pointer
Global item_Index%;Primary Key ID Management Counter

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type item
	;Purpose: 
	;Properties:
	Field ID%;
	;Field recyclerID%
	;Field copies%
	Field itemtype%;
	Field filename%;
	Field entity%;
	Field effect$;
	Field effecttarget%;
	Field effectduration%;
	Field effectvalue%;
	Field respawn%;
	Field respawndelay%;
End Type

;============================
;METHODS
;============================
Function itemStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	For this.item=Each item
		itemOpen()
	Next 
End Function

Function itemStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.item=Each item
		itemDelete(this)
	Next
End Function

Function itemNew.item()
	;Purpose: CONSTRUCTOR - Creates item Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: item Instance
	this.item=New item
	this\ID%=0
	this\itemtype%=0
	this\filename%=0
	this\entity%=0
	this\effect$=0
	this\effecttarget%=0
	this\effectduration%=0
	this\effectvalue%=0
	this\respawn%=0
	this\respawndelay%=0
	item_Index=item_Index+1
	this\id%=item_Index
	item_ID[this\id%]=this
	Return this
End Function

Function itemDelete(this.item)
	;Purpose: DESTRUCTOR - Removes item Instance
	;Parameters: item Instance
	;Return: None
	FreeEntity this\entity%
	Delete this
End Function

Function itemUpdate()
	;Purpose: Main Loop Update. Updates all item Instances.
	;Parameters: None
	;Return: None
	For this.item=Each item
		;code stuff here
	Next
End Function	

Function itemWrite(this.item,itemfile)
	;Purpose: Writes item Property Values to File
	;Parameters: item Instance, itemfile=filehandle
	;Return: None
	WriteInt(itemfile,this\ID%)
	WriteInt(itemfile,this\itemtype%)
	WriteInt(itemfile,this\filename%)
	WriteInt(itemfile,this\entity%)
	WriteString(itemfile,this\effect$)
	WriteInt(itemfile,this\effecttarget%)
	WriteInt(itemfile,this\effectduration%)
	WriteInt(itemfile,this\effectvalue%)
	WriteInt(itemfile,this\respawn%)
	WriteInt(itemfile,this\respawndelay%)
End Function

Function itemRead.item(itemfile)
	;Purpose: Reads File To item Property Values
	;Parameters: itemfile=filehandle
	;Return: item Instance
	this.item=New item
	this\ID%=ReadInt(itemfile)
	this\filename%=ReadInt(itemfile)
	this\entity%=ReadInt(itemfile)
	this\effect$=ReadString(itemfile)
	this\effecttarget%=ReadInt(itemfile)
	this\effectduration%=ReadInt(itemfile)
	this\effectvalue%=ReadInt(itemfile)
	this\respawn%=ReadInt(itemfile)
	this\respawndelay%=ReadInt(itemfile)
	item_ID[this\id%]=this
	Return this
End Function

Function itemSave(itemfilename$="item.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: itemfilename$= Name of File
	;Return: None
	itemfile=WriteFile(itemfilename$)
	For this.item= Each item
		itemWrite(this,itemfile)
	Next
	CloseFile(itemfile)
End Function

Function itemOpen(itemfilename$="item.dat")
	;Purpose: Opens a Object Property file
	;Parameters: itemfilename$=Name of Property File
	;Return: None
	itemfile=ReadFile(itemfilename$)
	Repeat
		itemRead(itemfile)
	Until Eof(itemfile)
	CloseFile(itemfile)
End Function

Function itemCopy.item(this.item)
	;Purpose: Creates a Copy of a item Instance
	;Parameters: item Instance
	;Return: Copy of item Instance
	copy.item=itemNew()
	copy\ID%=this\ID%
	copy\filename%=this\filename%
	copy\entity%=this\entity%
	copy\effect$=this\effect$
	copy\effecttarget%=this\effecttarget%
	copy\effectduration%=this\effectduration%
	copy\effectvalue%=this\effectvalue%
	copy\respawn%=this\respawn%
	copy\respawndelay%=this\respawndelay%
	Return copy
End Function

Function itemMimic(this.item,mimic.item)
	;Purpose: Copies Property Values from one item Instance To another
	;Parameters: item Instance
	;Return: None
	mimic\ID%=this\ID%
	mimic\filename%=this\filename%
	mimic\entity%=this\entity%
	mimic\effect$=this\effect$
	mimic\effecttarget%=this\effecttarget%
	mimic\effectduration%=this\effectduration%
	mimic\effectvalue%=this\effectvalue%
	mimic\respawn%=this\respawn%
	mimic\respawndelay%=this\respawndelay%
End Function

Function itemCreate%(itemID%,itemfilename%,itementity%,itemeffect$,itemeffecttarget%,itemeffectduration%,itemeffectvalue%,itemrespawn%,itemrespawndelay%, itemrx#, itemry#, itemrz#)
	;Purpose: Creates an Object and returns it ID
	;Parameters: Object's Properties
	;Return: Object's ID%
	this.item=itemNew()
	this\ID%=itemID%
	this\filename%=itemfilename%
	this\entity%=itementity%
	this\effect$=itemeffect$
	this\effecttarget%=itemeffecttarget%
	this\effectduration%=itemeffectduration%
	this\effectvalue%=itemeffectvalue%
	this\respawn%=itemrespawn%
	this\respawndelay%=itemrespawndelay%
	Return this\id
End Function

Function itemDestroy(itemid%)
	;Purpose: Remove object by ID
	;Parameters: itemid%=Object's ID
	;Return: None
	itemDelete(item_ID[itemid%])
End Function

Function itemSet(this.item,itemID%,itemfilename%,itementity%,itemeffect$,itemeffecttarget%,itemeffectduration%,itemeffectvalue%,itemrespawn%,itemrespawndelay%, itemrx#, itemry#, itemrz#)
	;Purpose: Set item Property Values
	;Parameters: item Instance and Properties
	;Return: None
	this\ID%=itemID%
	this\filename%=itemfilename%
	this\entity%=itementity%
	this\effect$=itemeffect$
	this\effecttarget%=itemeffecttarget%
	this\effectduration%=itemeffectduration%
	this\effectvalue%=itemeffectvalue%
	this\respawn%=itemrespawn%
	this\respawndelay%=itemrespawndelay%
End Function

Function itemMethod.item(this.item)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function

Function itemDefaultDatWrite()
	;Purpose: to write default values for testing
	;Parameters: None
	;Returns: Nothing
	this.item=itemNew()
	this\ID%=HEALTH%
	this\entity%=CreateSphere()
		EntityColor this\entity%, 0, 0, 255
		EntityAlpha this\entity%, 0.6
		ScaleEntity this\entity%, 0.5, 0.5, 0.5
	this\effect$="health"
	this\effecttarget%=0
	this\effectduration%=0
	this\effectvalue%=111
	this\respawn%=0
	this\respawndelay%=30000 ;30 seconds
End Function 