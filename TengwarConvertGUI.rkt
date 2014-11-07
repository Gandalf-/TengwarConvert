#lang racket

;Gui for TengwarConvert.java

;OUTLINE
;----------------------------------------------------
; GUI gets input from user
; GUI creates Output.txt with input
; GUI calls TengwarConvert.java and passes Output.txt
; TengwarConvert.java runs conversion
; TengwarConvert prints output to Output_converted.txt
; GUI calls OpenOutput.vbs
; OpenOutput.vbs opens Output_converted.txt in notepad
; OpenOutput.vbs changes font to Tengwar Annatar

(require racket/gui)

;FILE IO
;-----------------------------------------------------
;Moves the file into a string
(define (file->listChars filename)
  (port->string (open-input-file filename)))

;Prints x to a file
(define (print-this x name)
  (call-with-output-file* name #:exists 'replace
                          (lambda (output-port)
                            (display x output-port))))

;INPUT/CONVERSION
;------------------------------------------------------------------------------
(define example "in aenchent tiems the rings of power wer crafted bie the elven-smiths")

(define (run-convert input)
  ;Print to file
  (print-this (string-append " " input) "Output.txt")
  ;Get conversion
  (define output
    (with-output-to-string (lambda ()
                             (system "java TengwarConvert Output.txt"))))
  ;Open converted text in notepad
  (system "cscript OpenOutput.vbs")
  ;Cleanup
  (delete-file "Output_converted.txt")
  (delete-file "Output.txt")
  )

;MAIN-WINDOW
;---------------------------------------------
(define main-frame 
  (new frame%
       (label "TengwarConvert")
       (stretchable-width #f)
       (stretchable-height #f)
       (min-width 400)
       (min-height 100)
       ))

(define input-panel (instantiate horizontal-panel% (main-frame)
                      (stretchable-height #f)
                      ))

(define panel (instantiate horizontal-panel% (main-frame)
                (stretchable-height #f)
                ))

(define quit-panel (instantiate horizontal-panel% (main-frame)
                (stretchable-height #f)
                ))

;GUI
(define input-field
  (new text-field%
       (label ">")
       (parent input-panel)
       ))

(define clear-button
  (new button%
       (label "Clear input")
       (parent panel)
       (horiz-margin 12)
       (callback
        (lambda (button event)
          (send input-field set-value " ")
          (send main-frame set-status-text "Ready")))
       ))

(define convert-button
  (new button%
       (label "Convert")
       (parent panel)
       (horiz-margin 0)
       (callback
        (lambda (button event)
          (send main-frame set-status-text "Working..")
          (run-convert (send input-field get-value))
          (send main-frame set-status-text "Done!")
          (sleep 2)
          (send main-frame set-status-text "Ready")))
       ))

(send input-field set-value " ")
(send main-frame create-status-line)
(send main-frame set-status-text "Ready")
(send main-frame show #t)