format ELF64
public Java_cc_jlom_Main_add
public Java_cc_jlom_Main_sub
public Java_cc_jlom_Main_triple_1sum
public Java_cc_jlom_Main_plus_1one
public Java_cc_jlom_Main_minus_12
public Java_cc_jlom_Main_print
public Java_cc_jlom_Main_do_1smth
public Java_cc_jlom_Main_prnt_1str
public Java_cc_jlom_Main_stat_1call
public getstatic_object_field

section '.text' executable align 16

macro getstatic owner, name, desc {
    lea rsi, [owner]
    lea rdx, [name]
    lea rcx, [desc]
    call getstatic_object_field
}
macro save_regs offset {
    mov [rsp+offset], rsi
    mov [rsp+offset+8], rdx
    mov [rsp+offset+16], rcx
    mov [rsp+offset+24], rdi
}
macro store_regs offset {
    mov rsi, [rsp+offset]
    mov rdx, [rsp+offset+8]
    mov rcx, [rsp+offset+16]
    mov rdi, [rsp+offset+24]
}
macro invokevirtual obj, desc, name, rdi_offset, args_count, [args] {
    mov rsi, obj
    mov rax, [rdi]
    call qword [rax+248]
    mov rdi, [rsp+rdi_offset]
    lea rcx, [desc]
    lea rdx, [name]
    mov rsi, rax
    mov rax, [rdi]
    call qword [rax+264]

    mov rdi, [rsp+rdi_offset]

    mov rsi, obj
    mov rdx, rax
    mov rax, [rdi]
    if args_count eq 1
        match first, args \{
            mov rcx, first
        \}
    else if args_count eq 2
        match a,b, args \{
            mov rcx, a
            mov r8,  b
        \}
    else if args_count eq 3
        match a,b,c, args \{
            mov rcx, a
            mov r8,  b
            mov r9,  c
        \}
    end if

    call qword [rax+488]
}

Java_cc_jlom_Main_add:
push rbp
mov rbp, rsp
push rbx
push r12
sub rsp, 56
mov [rsp + 0], edx
mov [rsp + 4], ecx
mov ebx, [rsp+0]
mov r12d, [rsp+4]
add ebx, r12d
mov eax, ebx
add rsp, 56
pop r12
pop rbx
leave
ret

Java_cc_jlom_Main_sub:
push rbp
mov rbp, rsp
push rbx
push r12
sub rsp, 56
mov [rsp + 0], edx
mov [rsp + 4], ecx
mov ebx, [rsp+0]
mov r12d, [rsp+4]
sub ebx, r12d
mov eax, ebx
add rsp, 56
pop r12
pop rbx
leave
ret

Java_cc_jlom_Main_triple_1sum:
push rbp
mov rbp, rsp
push rbx
push r12
sub rsp, 56
mov [rsp + 0], edx
mov [rsp + 4], ecx
mov ebx, [rsp+0]
mov r12d, [rsp+4]
add ebx, r12d
mov r12d, [rsp+0]
add ebx, r12d
mov eax, ebx
add rsp, 56
pop r12
pop rbx
leave
ret

Java_cc_jlom_Main_plus_1one:
push rbp
mov rbp, rsp
push rbx
push r12
sub rsp, 40
mov [rsp + 0], edx
mov ebx, [rsp+0]
mov r12d, 1
add ebx, r12d
mov eax, ebx
add rsp, 40
pop r12
pop rbx
leave
ret

Java_cc_jlom_Main_minus_12:
push rbp
mov rbp, rsp
push rbx
push r12
sub rsp, 40
mov [rsp + 0], edx
mov ebx, [rsp+0]
mov r12d, 2
sub ebx, r12d
mov eax, ebx
add rsp, 40
pop r12
pop rbx
leave
ret

Java_cc_jlom_Main_print:
push rbp
mov rbp, rsp
push rbx
push r12
push r13
sub rsp, 56
mov [rsp + 0], edx
mov [rsp + 4], ecx
save_regs 8
getstatic S2078463709,S110414,S1603068950
store_regs 8
mov rbx, rax

mov r12d, [rsp+0]
save_regs 8
invokevirtual rbx, S1263150, S314717969, 32,1, r12
store_regs 8

save_regs 8
getstatic S2078463709,S110414,S1603068950
store_regs 8
mov rbx, rax

mov r12d, [rsp+4]
save_regs 8
invokevirtual rbx, S1263150, S314717969, 32,1, r12
store_regs 8

save_regs 8
getstatic S2078463709,S110414,S1603068950
store_regs 8
mov rbx, rax

mov r12d, [rsp+0]
mov r13d, [rsp+4]
sub r12d, r13d
save_regs 8
invokevirtual rbx, S1263150, S314717969, 32,1, r12
store_regs 8

xor eax, eax
add rsp, 56
pop r13
pop r12
pop rbx
leave
ret

Java_cc_jlom_Main_do_1smth:
push rbp
mov rbp, rsp
push rbx
push r12
sub rsp, 64
mov [rsp + 0], edx
mov ebx, [rsp+0]
mov r12d, 1
add ebx, r12d
mov [rsp + 4], ebx
mov ebx, [rsp+4]
mov r12d, [rsp+0]
add ebx, r12d
mov [rsp + 8], ebx
save_regs 16
getstatic S2078463709,S110414,S1603068950
store_regs 16
mov rbx, rax

mov r12d, [rsp+8]
save_regs 16
invokevirtual rbx, S1263150, S314717969, 40,1, r12
store_regs 16

mov ebx, [rsp+8]
mov r12d, [rsp+0]
sub ebx, r12d
mov [rsp + 12], ebx
save_regs 16
getstatic S2078463709,S110414,S1603068950
store_regs 16
mov rbx, rax

mov r12d, [rsp+12]
save_regs 16
invokevirtual rbx, S1263150, S314717969, 40,1, r12
store_regs 16

xor eax, eax
add rsp, 64
pop r12
pop rbx
leave
ret

Java_cc_jlom_Main_prnt_1str:
push rbp
mov rbp, rsp
push rbx
push r12
sub rsp, 48
mov [rsp+8], rsi
mov [rsp+16], rdx
mov [rsp+24], rcx
mov [rsp+32], rdi
lea rsi, [S577281058]
mov rax, [rdi]
call qword [rax+1336]
mov rbx, rax
mov rsi, [rsp+8]
mov rdx, [rsp+16]
mov rcx, [rsp+24]
mov rdi, [rsp+32]

mov [rsp + 0], rbx
save_regs 8
getstatic S2078463709,S110414,S1603068950
store_regs 8
mov rbx, rax

mov r12, [rsp+0]
save_regs 8
invokevirtual rbx, S1428966913, S314717969, 32,1, r12
store_regs 8

xor eax, eax
add rsp, 48
pop r12
pop rbx
leave
ret

Java_cc_jlom_Main_stat_1call:
push rbp
mov rbp, rsp
push rbx
push r12
push r13
sub rsp, 40
save_regs 4
getstatic S2078463709,S110414,S1603068950
store_regs 4
mov rbx, rax

mov [rsp+4], rsi
mov [rsp+12], rdx
mov [rsp+20], rcx
mov [rsp+28], rdi
lea rsi, [S11073502]
mov rax, [rdi]
call qword [rax+1336]
mov r12, rax
mov rsi, [rsp+4]
mov rdx, [rsp+12]
mov rcx, [rsp+20]
mov rdi, [rsp+28]

save_regs 4
invokevirtual rbx, S1428966913, S314717969, 28,1, r12
store_regs 4

mov ebx, 1
mov r12d, 5
save_regs 4
lea rsi, [S1266906361]
mov rax, [rdi]
call qword [rax+48]
mov r13, rax
mov rdi, [rsp+28]
lea rcx, [S39187080]
lea rdx, [S96417]
mov rsi, rax
mov rax, [rdi]
call qword [rax+904]
mov rdi, [rsp+28]
mov rsi, r13
mov rdx, rax
mov rcx, rbx
mov r8, r12
mov rax, [rdi]
call qword [rax+1032]
store_regs 4
mov r12, rax

mov [rsp + 0], r12d
save_regs 4
getstatic S2078463709,S110414,S1603068950
store_regs 4
mov r12, rax

mov ebx, [rsp+0]
save_regs 4
invokevirtual r12, S1263150, S314717969, 28,1, rbx
store_regs 4

xor eax, eax
add rsp, 40
pop r13
pop r12
pop rbx
leave
ret

getstatic_object_field:

    push rbp
    push rbx
    sub rsp, 24

    push rdx
    push rcx

    mov rax, [rdi] ; env
    mov rbx, rdi
    call qword [rax+48]

    pop rcx
    mov rdi, rbx
    pop rdx
    mov rbp, rax
    mov rax, [rbx] ; env*
    mov rsi, rbp ; cls
    ; rdi env, rsi cls, rdx name, rcx desc
    call qword [rax+1152]
    ;; fid


    mov rsi, rbp ; cls
    mov rdi, rbx ; env
    mov rdx, rax ; fid
    mov rax, [rbx] ; env*
    ; rdi env, rsi cls, rdx fid
    mov rax, [rax+1160] ; rax out

    add rsp, 24
    pop rbx
    pop rbp
    jmp rax


section '.data' writeable
S2078463709: db "java/lang/System", 0
S1263150: db "(I)V", 0
S1603068950: db "Ljava/io/PrintStream;", 0
S314717969: db "println", 0
S11073502: db "Call add method", 0
S39187080: db "(II)I", 0
S1266906361: db "cc/jlom/Main", 0
S577281058: db "sTR PriNT", 0
S1428966913: db "(Ljava/lang/String;)V", 0
S110414: db "out", 0
S96417: db "add", 0
