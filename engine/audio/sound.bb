;============================
;SOUND CLASS
;Generated with TypeWriterIV
;============================

;============================
;INCLUDES
;============================

;============================
;CONSTANTS
;============================
Const SOUND_MAX%=64
Const SOUND_STATE_STOP=0
Const SOUND_STATE_PLAYING=1
Const SOUND_STATE_DYING=2
Const SOUND_STATE_VOLUMEADJUST=3
Const SOUND_STATE_PITCHADJUST=4
;============================
;GLOBALS
;============================
Global sound_stackPTR%; Pointer for sound_stack array
Global sound_Activeimagemap.sound;

;============================
;ARRAYS
;============================
Dim sound_stack.sound(SOUND_MAX%)

;============================
;OBJECT
;============================
Type sound
	;Purpose: SoundFx and Music Object
	;Properties:
	Field audioID%;
	Field channel%;
	Field volume#;
	Field volumeFactor#
	Field volumeCTR#
	Field pitch%;
	Field pitchFactor%
	Field pitchCTR%
	Field pan#;
	Field lifeCTR%
	Field state%;
End Type

;============================
;METHODS
;============================
Function soundStart()
	;Purpose: Initialize Class
	;Parameters: TBD
	;Return: None
	For soundloop=1 To SOUND_MAX
		soundStackPush(soundNew())	
	Next		
End Function

Function soundStop()
	;Purpose: Shutdown Class
	;Parameters: TBD
	;Return: None
	For this.sound=Each sound
		soundDelete(this)
	Next
End Function

Function soundNew.sound()
	;Purpose: CONSTRUCTOR - Creates sound Instance; Sets Default Property Values
	;Parameters: TBD
	;Return: sound Instance
	this.sound=New sound
	this\audioID%=0
	this\channel%=0
	this\volume#=0.0
	this\pitch%=0
	this\pan#=0.0
	this\state%=0
	Return this
End Function

Function soundDelete(this.sound)
	;Purpose: DESTRUCTOR - Removes sound Instance
	;Parameters: sound Instance
	;Return: None
	this\pan#=0.0
	this\volume#=0.0
	Delete this
End Function

Function soundUpdate()
	;Purpose: Main Loop Update. Updates all sound Instances.
	;Parameters: None
	;Return: None
	For this.sound=Each sound
		audio.audio=audio_ID[this\audioID]
		Select this\state
			Case SOUND_STATE_PLAYING
				this\lifeCTR=this\lifeCTR-1
				If Not this\lifeCTR this\state=SOUND_STATE_DYING				
			Case SOUND_STATE_DYING
				recyclerAudioPush(recycler_ID[audio\recyclerID%],this\channel)
				soundStackPush(this)
				this\state=SOUND_STATE_STOP
			Case SOUND_STATE_VOLUMEADJUST
				this\volumeCTR=this\volumeCTR+this\volumeFactor#
				channelVolume(this\channel,this\volumeCTR)
				this\lifeCTR=this\lifeCTR-1
				If Not this\lifeCTR this\state=SOUND_STATE_DYING
			Case SOUND_STATE_PITCHADJUST
				delay 2000
				this\pitchCTR=this\pitchCTR+this\pitchFactor
				channelPitch(this\channel,this\pitchCTR)
				this\lifeCTR=this\lifeCTR-1
				If Not this\lifeCTR this\state=SOUND_STATE_DYING
		End Select
	
	Next
End Function

Function soundWrite(this.sound,soundfile)
	;Purpose: Writes sound Property Values to File
	;Parameters: sound Instance, soundfile=filehandle
	;Return: None
	WriteInt(soundfile,this\audioID%)
	WriteInt(soundfile,this\channel%)
	WriteFloat(soundfile,this\volume#)
	WriteInt(soundfile,this\pitch%)
	WriteFloat(soundfile,this\pan#)
	WriteInt(soundfile,this\state%)
End Function

Function soundRead.sound(soundfile)
	;Purpose: Reads File To sound Property Values
	;Parameters: soundfile=filehandle
	;Return: sound Instance
	this.sound=New sound
	this\audioID%=ReadInt(soundfile)
	this\channel%=ReadInt(soundfile)
	this\volume#=ReadFloat(soundfile)
	this\pitch%=ReadInt(soundfile)
	this\pan#=ReadFloat(soundfile)
	this\state%=ReadInt(soundfile)
	Return this
End Function

Function soundSave(soundfilename$="sound.dat")
	;Purpose: Saves all Object Properties to a file
	;Parameters: soundfilename$= Name of File
	;Return: None
	soundfile=WriteFile(soundfilename$)
	For this.sound= Each sound
		soundWrite(this,soundfile)
	Next
	CloseFile(soundfile)
End Function

Function soundOpen(soundfilename$="sound.dat")
	;Purpose: Opens a Object Property file
	;Parameters: soundfilename$=Name of Property File
	;Return: None
	soundfile=ReadFile(soundfilename$)
	Repeat
		soundRead(soundfile)
	Until Eof(soundfile)
	CloseFile(soundfile)
End Function

Function soundCopy.sound(this.sound)
	;Purpose: Creates a Copy of a sound Instance
	;Parameters: sound Instance
	;Return: Copy of sound Instance
	copy.sound=soundNew()
	copy\audioID%=this\audioID%
	copy\channel%=this\channel%
	copy\volume#=this\volume#
	copy\pitch%=this\pitch%
	copy\pan#=this\pan#
	copy\state%=this\state%
	Return copy
End Function

Function soundMimic(this.sound,mimic.sound)
	;Purpose: Copies Property Values from one sound Instance To another
	;Parameters: sound Instance
	;Return: None
	mimic\audioID%=this\audioID%
	mimic\channel%=this\channel%
	mimic\volume#=this\volume#
	mimic\pitch%=this\pitch%
	mimic\pan#=this\pan#
	mimic\state%=this\state%
End Function

Function soundSet(this.sound,soundaudioID%,soundchannel,soundlifeCTR,soundstate)
	;Purpose: Set sound Property Values
	;Parameters: sound Instance and Properties
	;Return: None
	this\audioID%=soundaudioID%
	this\channel%=soundchannel%
	this\lifeCTR%=soundlifeCTR%
	this\state%=soundstate%
End Function

Function soundStackPush(this.sound)
	;Purpose: Pushes sound object into sound_stack
	;Parameters: TBD
	;Return: TBD
	sound_stack(sound_stackPTR%)=this
	sound_stackPTR%=sound_stackPTR%+1	
End Function

Function soundStackPop.sound()
	;Purpose: Pops sound object out of sound_stack
	;Parameters: TBD
	;Return: TBD
	sound_stackPTR%=sound_stackPTR%-1	
	Return sound_stack(sound_stackPTR%)	
End Function

Function soundMusicKill(this.sound)
	If channelPlaying(this\channel) stopchannel(this\channel)
	this\state=SOUND_STATE_STOP
	soundStackPush(this)
End Function

Function soundMethod.sound(this.sound)
	;Purpose: Method Template
	;Parameters: TBD
	;Return: TBD
	;code stuff here...
	Return this
End Function
