<?php

namespace App\Imports;
use App\Models\Answer;
use Illuminate\Support\Collection;
use Maatwebsite\Excel\Concerns\ToCollection;
use Maatwebsite\Excel\Concerns\WithHeadingRow;
use Illuminate\Support\Facades\Log;


class AnswerImport implements ToCollection, WithHeadingRow
{
    /**
    * @param Collection $collection
    */
    public function collection(Collection $collection)
    {
        foreach ($collection as $row)
        {
            // Log the row data for debugging purposes
            Log::info('Importing row: ', $row->toArray());

            // Check if question_id is present
            if (!isset($row['question_id']) || is_null($row['question_id'])) {
                Log::error('Question ID is missing or null for row: ', $row->toArray());
                continue; // Skip this row and move to the next
            }

            Answer::create([
                'question_id' => $row['question_id'],
                'answer_id' => $row['answer_id'],
                'answer_text' => $row['answer_text'],
            ]);
        }
    
    }
}
