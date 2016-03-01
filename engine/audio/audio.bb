;============================
;AUDIO CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================
;Include "sound.bb"
;Include "../reactor/recycler.bb"

;============================
;CONSTANTS
;============================
Const AUDIO_MAX%=64
Const AUDIO_AUDIOTYPE_SOUND%=1
Const AUDIO_AUDIOTYPE_MUSIC%=2
Const AUDIO_AUDIOTYPE_CDTRACK%=3
Const AUDIO_MOOD_ACTION=1
Const AUDIO_MOOD_SAD=2
Const AUDIO_MOOD_THRILLER=3

;============================
;GLOBALS
;============================
Global audio_ID.audio[AUDIO_MAX%];Primary Key Object Pointer
Global audio_Index%;Primary Key ID Management Counter

;============================
;ARRAYS
;============================

;============================
;OBJECT
;============================
Type audio
	;Purpose: 
	;Properties:
	Field ID%;
	Field recyclerID%;
	Field copies%;	
	Field audiotype%;
	Field filename$;
	Field sound%
	Field track%;
	Field mode%;
	Field mood%;
End Type

;============================
;METHODS
;============================
Function audioStart(audiofilename$="audio.dat")
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	audioOpen(audiofilename)
	For this.audio = Each audio
		Select this\audiotype
			Case AUDIO_AUDIOTYPE_SOUND%
				;create recycler and store object key
				this\recyclerID%=recyclerCreate(this\copies%)
				;create recycler entity copies for stack push/pop 
				recyclerAudioCopy(recycler_ID[this\recyclerID%],this\filename$,this\copies)
			Case AUDIO_AUDIOTYPE_MUSIC%
			Case AUDIO_AUDIOTYPE_CDTRACK%
		End Select
	Next
End Function

Function audioStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.audio=Each audio
		audioDelete(this)
	Next
End Function

Function audioNew.audio()
	;Purpose: CONSTRUCTOR - Creates audio Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: audio Instance
	this.audio=New audio
	this\ID%=0
	this\recyclerID%=0
	this\copies%=0	
	this\audiotype%=0
	this\filename$=""
	this\track%=0
	this\mode%=0
	this\mood%=0
	audio_Index=audio_Index+1
	this\id%=audio_Index
	audio_ID[this\id%]=this
	Return this
End Function

Function audioDelete(this.audio)
	;Purpose: DESTRUCTOR - Removes audio Instance
	;Parameters: audio Instance
	;Return: None
	this\filename$=""
	Delete this
End Function

Function audioUpdate()
	;Purpose: Main Loop Update. Updates all audio Instances.
	;Parameters: None
	;Return: None
	For this.audio=Each audio
		;Do stuff to 'this' here!
	Next
End Function

Function audioWrite(this.audio,audiofile)
	;Purpose: Writes audio Property Values to File
	;Parameters: audio Instance, audiofile=filehandle
	;Return: None
	WriteInt(audiofile,this\ID%)
	WriteInt(audiofile,this\recyclerID%)
	WriteInt(audiofile,this\copies%)	
	WriteInt(audiofile,this\audiotype%)
	WriteString(audiofile,this\filename$)
	WriteInt(audiofile,this\track%)
	WriteInt(audiofile,this\mode%)
	WriteInt(audiofile,this\mood%)
End Function

Function audioRead.audio(audiofile)
	;Purpose: Reads File To audio Property Values
	;Parameters: audiofile=filehandle
	;Return: audio Instance
	this.audio=New audio
	this\ID%=ReadInt(audiofile)
	this\recyclerID%=ReadInt(audiofile)
	this\copies%=ReadInt(audiofile)	
	this\audiotype%=ReadInt(audiofile)
	this\filename$=ReadString(audiofile)
	this\track%=ReadInt(audiofile)
	this\mode%=ReadInt(audiofile)
	this\mood%=ReadInt(audiofile)
	audio_ID[this\id%]=this
	Return this
End Function

Function audioSave(audiofilename$="audio.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: audiofilename$= Name of File
	;Return: None
	audiofile=WriteFile(audiofilename$)
	For this.audio= Each audio
		audioWrite(this,audiofile)
	Next
	CloseFile(audiofile)
End Function

Function audioOpen(audiofilename$="audio.dat")
	;Purpose: Opens a Object Property file
	;Parameters: audiofilename$=Name of Property File
	;Return: None
	audiofile=ReadFile(audiofilename$)
	Repeat
		audioRead(audiofile)
	Until Eof(audiofile)
	CloseFile(audiofile)
End Function

Function audioCopy.audio(this.audio)
	;Purpose: Creates a Copy of a audio Instance
	;Parameters: audio Instance
	;Return: Copy of audio Instance
	copy.audio=audioNew()
	copy\ID%=this\ID%
	copy\recyclerID%=this\recyclerID%
	copy\copies%=this\copies%	
	copy\audiotype%=this\audiotype%
	copy\filename$=this\filename$
	copy\track%=this\track%
	copy\mode%=this\mode%
	copy\mood%=this\mood%
	Return copy
End Function

Function audioMimic(this.audio,mimic.audio)
	;Purpose: Copies Property Values from one audio Instance To another
	;Parameters: audio Instance
	;Return: None
	mimic\ID%=this\ID%
	mimic\recyclerID%=this\recyclerID%
	mimic\copies%=this\copies%	
	mimic\audiotype%=this\audiotype%
	mimic\filename$=this\filename$
	mimic\track%=this\track%
	mimic\mode%=this\mode%
	mimic\mood%=this\mood%
End Function

Function audioCreate%(audioID%,audiorecyclerID%,audiocopies%,audioaudiotype%,audiofilename$,audiotrack%,audiomode%,audiomood%)
	;Purpose: Creates an Object and returns it ID, all sound fx are loaded as 3D
	;Parameters: Object's Properties
	;Return: Object's ID%
	this.audio=audioNew()
	this\ID%=audioID%
	this\recyclerID%=audiorecyclerID%
	this\copies%=audiocopies%	
	this\audiotype%=audioaudiotype%
	this\filename$=audiofilename$
	this\track%=audiotrack%
	this\mode%=audiomode%
	this\mood%=audiomood%
	Return this\id
End Function

Function audioDestroy(audioid%)
	;Purpose: Remove object by ID
	;Parameters: audioid%=Object's ID
	;Return: None
	audioDelete(audio_ID[audioid%])
End Function

Function audioSet(this.audio,audioID%,audiorecyclerID%,audiocopies%,audioaudiotype%,audiofilename$,audiotrack%,audiomode%,audiomood%)
	;Purpose: Set audio Property Values
	;Parameters: audio Instance and Properties
	;Return: None
	this\ID%=audioID%
	this\recyclerID%=audiorecyclerID%
	this\copies%=audiocopies%	
	this\audiotype%=audioaudiotype%
	this\filename$=audiofilename$
	this\track%=audiotrack%
	this\mode%=audiomode%
	this\mood%=audiomood%
End Function

Function audioDefaultDatWrite()
	;Purpose: Creates default reactor dat file
	;Parameters: TBD
	;Return: reactor Instance
	
	;1
	this.audio=audioNew()
	this\copies%=16
	this\audiotype%=AUDIO_AUDIOTYPE_SOUND
	this\filename$="..\game\data\audio\sound\shoot.wav"
	this\mode%=0
	this\mood%=AUDIO_MOOD_ACTION
	
	;2
	this.audio=audioNew()
	this\copies%=16
	this\audiotype%=AUDIO_AUDIOTYPE_SOUND
	this\filename$="..\game\data\audio\sound\explosion.mp3"
	this\mode%=0
	this\mood%=AUDIO_MOOD_ACTION
	
	;3
	this.audio=audioNew()
	this\copies%=8
	this\audiotype%=AUDIO_AUDIOTYPE_SOUND
	this\filename$="..\game\data\audio\sound\cooling.wav"
	this\mode%=0
	this\mood%=AUDIO_MOOD_ACTION	
	
	;4
	this.audio=audioNew()
	this\copies%=8
	this\audiotype%=AUDIO_AUDIOTYPE_SOUND
	this\filename$="..\game\data\audio\sound\empty.wav"
	this\mode%=0
	this\mood%=AUDIO_MOOD_ACTION
	
	;5
	this.audio=audioNew()
	this\copies%=12
	this\audiotype%=AUDIO_AUDIOTYPE_SOUND
	this\filename$="..\game\data\audio\sound\load.wav"
	this\mode%=0
	this\mood%=AUDIO_MOOD_ACTION	
	
	;6
	this.audio=audioNew()
	this\copies%=8
	this\audiotype%=AUDIO_AUDIOTYPE_SOUND
	this\filename$="..\game\data\audio\sound\shoot2.wav"
	this\mode%=0
	this\mood%=AUDIO_MOOD_ACTION	
	
	;7
	this.audio=audioNew()
	this\copies%=1
	this\audiotype%=AUDIO_AUDIOTYPE_MUSIC
	this\filename$="..\game\data\audio\music\darkloop.mp3"
	this\mode%=0
	this\mood%=AUDIO_MOOD_ACTION
	
	;8
	this.audio=audioNew()
	this\copies%=8
	this\audiotype%=AUDIO_AUDIOTYPE_SOUND
	this\filename$="..\game\data\audio\sound\shock.wav"
	this\mode%=0
	this\mood%=AUDIO_MOOD_ACTION	

	;9
	this.audio=audioNew()
	this\copies%=8
	this\audiotype%=AUDIO_AUDIOTYPE_SOUND
	this\filename$="..\game\data\audio\sound\charge.wav"
	this\mode%=0
	this\mood%=AUDIO_MOOD_ACTION	

	audioSave()
End Function

Function audioMethod.audio(this.audio)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function

If gamedatawrite audioDefaultDatWrite