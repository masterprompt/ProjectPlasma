;============================
;GAME CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const GAME_MAX%=1
Const GAME_COLLISIONTYPE_NONE=0
Const GAME_COLLISIONTYPE_LEVEL=1
Const GAME_COLLISIONTYPE_TERRAIN=2
Const GAME_COLLISIONTYPE_PLAYER=4
Const GAME_COLLISIONTYPE_PROJECTILE=5
Const GAME_COLLISIONTYPE_PHYSICS=6
Const GAME_COLLISIONTYPE_PROP=7
Const GAME_COLLISIONMETHOD_ELLIPSOID=1
Const GAME_COLLISIONMETHOD_POLYGON=2
Const GAME_COLLISIONMETHOD_BOX=3
Const GAME_COLLISIONRESPONSE_STOP=1
Const GAME_COLLISIONRESPONSE_SLIDE=2
Const GAME_COLLISIONRESPONSE_SLIDENOSLOPE=3
Const GAME_STATE_HOLDING=0
Const GAME_STATE_START=1
Const GAME_STATE_PLAY=2
Const GAME_STATE_PLAYING=3
Const GAME_STATE_PAUSE=4
Const GAME_STATE_STOP=5
Const GAME_STATE_EXIT=6
Const GAME_STATE_EDIT=7

;============================
;GLOBALS
;============================
Global game.game
Global game_State%=GAME_STATE_START

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type game
	;Purpose: Game Configuration
	;Properties:
	Field rootdirectory$;
	Field screenwidth%;
	Field screenheight%;
	Field screendepth%;
	Field screenmode%;
	Field musicvolume#;
	Field soundvolume#;
	Field playmode%;
End Type

;============================
;METHODS
;============================
Function gameStart()
	;Purpose: Initialize Class; creates single game instance
	;Parameters: TBD
	;Return: None

	;load default
	AppTitle ("TeamPLASMA FPS")
	this.game=gameNew()
	;set graphic mode
	Graphics3D(this\screenwidth%,this\screenheight%,this\screendepth%,this\screenmode%)
	;setup double buffering
	SetBuffer(BackBuffer())
	;collision setup
	Collisions(GAME_COLLISIONTYPE_PLAYER,GAME_COLLISIONTYPE_LEVEL,GAME_COLLISIONMETHOD_POLYGON,GAME_COLLISIONRESPONSE_SLIDE)
	Collisions(GAME_COLLISIONTYPE_PLAYER,GAME_COLLISIONTYPE_TERRAIN,GAME_COLLISIONMETHOD_POLYGON,GAME_COLLISIONRESPONSE_SLIDE)
	Collisions(GAME_COLLISIONTYPE_PLAYER,GAME_COLLISIONTYPE_PROP,GAME_COLLISIONMETHOD_ELLIPSOID,GAME_COLLISIONRESPONSE_SLIDE)
	Collisions(GAME_COLLISIONTYPE_PROJECTILE,GAME_COLLISIONTYPE_LEVEL,GAME_COLLISIONMETHOD_POLYGON,GAME_COLLISIONRESPONSE_STOP)
	Collisions(GAME_COLLISIONTYPE_PROJECTILE,GAME_COLLISIONTYPE_TERRAIN,GAME_COLLISIONMETHOD_POLYGON,GAME_COLLISIONRESPONSE_STOP)	
	Collisions(GAME_COLLISIONTYPE_PROJECTILE,GAME_COLLISIONTYPE_PLAYER,GAME_COLLISIONMETHOD_ELLIPSOID,GAME_COLLISIONRESPONSE_STOP)
	Collisions(GAME_COLLISIONTYPE_PROJECTILE,GAME_COLLISIONTYPE_PROP,GAME_COLLISIONMETHOD_ELLIPSOID,GAME_COLLISIONRESPONSE_STOP)		
	Collisions(GAME_COLLISIONTYPE_PROJECTILE,GAME_COLLISIONTYPE_PROJECTILE,GAME_COLLISIONMETHOD_ELLIPSOID,GAME_COLLISIONRESPONSE_STOP)
	Collisions(GAME_COLLISIONTYPE_PHYSICS,GAME_COLLISIONTYPE_LEVEL,GAME_COLLISIONMETHOD_POLYGON,GAME_COLLISIONRESPONSE_SLIDE)
	Collisions(GAME_COLLISIONTYPE_PROP,GAME_COLLISIONTYPE_LEVEL,GAME_COLLISIONMETHOD_POLYGON,GAME_COLLISIONRESPONSE_STOP)
	Collisions(GAME_COLLISIONTYPE_PROP,GAME_COLLISIONTYPE_TERRAIN,GAME_COLLISIONMETHOD_POLYGON,GAME_COLLISIONRESPONSE_STOP)
	game=this
End Function

Function gameStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.game=Each game
		gameDelete(this)
	Next
End Function

Function gameNew.game()
	;Purpose: CONSTRUCTOR - Creates game Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: game Instance
	this.game=New game
	this\rootdirectory$=CurrentDir$()
	this\screenwidth%=640
	this\screenheight%=480
	this\screendepth%=32
	this\screenmode%=2
	this\musicvolume#=1.0
	this\soundvolume#=1.0
	this\playmode%=1
	Return this
End Function

Function gameDelete(this.game)
	;Purpose: DESTRUCTOR - Removes game Instance
	;Parameters: game Instance
	;Return: None
	Delete this
End Function

Function gameUpdate()
	;Purpose: Main Loop Update. Updates all game Instances.
	;Parameters: None
	;Return: None
	For this.game=Each game
		Select game_State%
			Case GAME_STATE_START,GAME_STATE_PAUSE
				ShowPointer()
				imagemap_ID[1]\state=IMAGEMAP_STATE_ACTIVE
				sound_Activeimagemap=soundStackPop()
				soundSet(sound_Activeimagemap,imagemap_ID[1]\audioID,PlayMusic(audio_ID[imagemap_ID[1]\audioID]\filename$),-1,SOUND_STATE_PLAYING)
				game_State=GAME_STATE_HOLDING
			Case GAME_STATE_PLAY
				HidePointer()
				imagemap_ID[imagemap_Active]\state=IMAGEMAP_STATE_NOTACTIVE
				soundMusicKill(sound_Activeimagemap)
				game_State=GAME_STATE_PLAYING
		End Select
	Next
End Function

Function gameWrite(this.game,gamefile)
	;Purpose: Writes game Property Values to File
	;Parameters: game Instance, gamefile=filehandle
	;Return: None
	WriteString(gamefile,this\rootdirectory$)
	WriteInt(gamefile,this\screenwidth%)
	WriteInt(gamefile,this\screenheight%)
	WriteInt(gamefile,this\screendepth%)
	WriteInt(gamefile,this\screenmode%)
	WriteFloat(gamefile,this\musicvolume#)
	WriteFloat(gamefile,this\soundvolume#)
	WriteInt(gamefile,this\playmode%)
End Function

Function gameRead.game(gamefile)
	;Purpose: Reads File To game Property Values
	;Parameters: gamefile=filehandle
	;Return: game Instance
	this.game=New game
	this\rootdirectory$=ReadString(gamefile)
	this\screenwidth%=ReadInt(gamefile)
	this\screenheight%=ReadInt(gamefile)
	this\screendepth%=ReadInt(gamefile)
	this\screenmode%=ReadInt(gamefile)
	this\musicvolume#=ReadFloat(gamefile)
	this\soundvolume#=ReadFloat(gamefile)
	this\playmode%=ReadInt(gamefile)
	Return this
End Function

Function gameSave(gamefilename$="game.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: gamefilename$= Name of File
	;Return: None
	gamefile=WriteFile(gamefilename$)
	For this.game= Each game
		gameWrite(this,gamefile)
	Next
	CloseFile(gamefile)
End Function

Function gameOpen(gamefilename$="game.dat")
	;Purpose: Opens a Object Property file
	;Parameters: gamefilename$=Name of Property File
	;Return: None
	gamefile=ReadFile(gamefilename$)
	Repeat
		gameRead(gamefile)
	Until Eof(gamefile)
	CloseFile(gamefile)
End Function

Function gameCopy.game(this.game)
	;Purpose: Creates a Copy of a game Instance
	;Parameters: game Instance
	;Return: Copy of game Instance
	copy.game=gameNew()
	copy\rootdirectory$=this\rootdirectory$
	copy\screenwidth%=this\screenwidth%
	copy\screenheight%=this\screenheight%
	copy\screendepth%=this\screendepth%
	copy\screenmode%=this\screenmode%
	copy\musicvolume#=this\musicvolume#
	copy\soundvolume#=this\soundvolume#
	copy\playmode%=this\playmode%
	Return copy
End Function

Function gameMimic(this.game,mimic.game)
	;Purpose: Copies Property Values from one game Instance To another
	;Parameters: game Instance
	;Return: None
	mimic\rootdirectory$=this\rootdirectory$
	mimic\screenwidth%=this\screenwidth%
	mimic\screenheight%=this\screenheight%
	mimic\screendepth%=this\screendepth%
	mimic\screenmode%=this\screenmode%
	mimic\musicvolume#=this\musicvolume#
	mimic\soundvolume#=this\soundvolume#
	mimic\playmode%=this\playmode%
End Function

Function gameSet(this.game,gamerootdirectory$,gamescreenwidth%,gamescreenheight%,gamescreendepth%,gamescreenmode%,gamemusicvolume#,gamesoundvolume#,gameplaymode%)
	;Purpose: Set game Property Values
	;Parameters: game Instance and Properties
	;Return: None
	this\rootdirectory$=gamerootdirectory$
	this\screenwidth%=gamescreenwidth%
	this\screenheight%=gamescreenheight%
	this\screendepth%=gamescreendepth%
	this\screenmode%=gamescreenmode%
	this\musicvolume#=gamemusicvolume#
	this\soundvolume#=gamesoundvolume#
	this\playmode%=gameplaymode%
End Function

Function gameMethod.game(this.game)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function