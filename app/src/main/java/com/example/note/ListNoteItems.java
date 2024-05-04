package com.example.note;

public class ListNoteItems {
        private String description;
        private boolean isChecked;

        public ListNoteItems(String description, boolean isChecked) {
            this.description = description;
            this.isChecked = isChecked;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
}
