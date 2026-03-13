package com.minimarket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class BackupController {

    @GetMapping("/api/backup")
    public void backupDatabase(HttpServletResponse response) {

        try {

            String dbName = "minimarket";
            String user = "postgres";

            String timestamp =
                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            String backupFile = "backup_" + timestamp + ".sql";

            File folder = new File("backup");
            if(!folder.exists()){
                folder.mkdir();
            }

            File file = new File(folder, backupFile);

            ProcessBuilder pb = new ProcessBuilder(
                    "C:\\Program Files\\PostgreSQL\\18\\bin\\pg_dump.exe",
                    "-U",
                    user,
                    dbName,
                    "-f",
                    file.getAbsolutePath()
            );

            pb.environment().put("PGPASSWORD", "123456");

            Process process = pb.start();
            process.waitFor();

            response.setContentType("application/octet-stream");
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" + backupFile
            );

            try(FileInputStream fis = new FileInputStream(file);
                OutputStream os = response.getOutputStream()){

                byte[] buffer = new byte[4096];
                int len;

                while((len = fis.read(buffer)) > 0){
                    os.write(buffer,0,len);
                }

            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
System.out.println("Backup API called");
    }

}