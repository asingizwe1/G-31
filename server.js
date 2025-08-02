// server.js
import express from 'express';
import cors from 'cors';
import bodyParser from 'body-parser';
import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
dotenv.config();

const { SUPABASE_URL, SUPABASE_SERVICE_KEY, PORT } = process.env;
const supabase = createClient(SUPABASE_URL, SUPABASE_SERVICE_KEY);

const app = express();
app.use(cors());
app.use(bodyParser.json());

// GET a voucher by code & recipient
app.get('/vouchers/:code', async (req, res) => {
    const { code } = req.params;
    const { recipient } = req.query;

    const { data, error } = await supabase
        .from('vouchers')
        .select('*')
        .eq('voucher_code', code)
        .eq('recipient_address', recipient)
        .single();

    if (error || !data) return res.status(404).json({ message: 'Voucher not found' });
    res.json(data);
});

// POST redeem: update status & txHash
app.post('/vouchers/:code/redeem', async (req, res) => {
    const { code } = req.params;
    const { txHash } = req.body;

    const { data, error } = await supabase
        .from('vouchers')
        .update({ status: 'redeemed', redemption_tx_hash: txHash, updated_at: 'now()' })
        .eq('voucher_code', code)
        .single();

    if (error) return res.status(400).json({ message: error.message });
    res.json(data);
});
// GET a voucher by asset_id
app.get('/vouchers/asset/:id', async (req, res) => {
    const { id } = req.params;

    const { data, error } = await supabase
        .from('vouchers')
        .select('*')
        .eq('asset_id', id)
        .single();

    if (error || !data) return res.status(404).json({ message: 'Voucher not found using asset_id' });
    res.json(data);
});

app.listen(PORT || 4000, () => {
    console.log(`API listening on http://localhost:${PORT || 4000}`);
});
