     .data
 i:  .byte   7 
 s:  .space  1
     .byte   0

     .text 
     lb  $t0, i
     add $a0, $t0, 0
     sb  $t0, s    
     la  $a0, s 
     li  $v0, 4
     syscall
