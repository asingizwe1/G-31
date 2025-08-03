// src/pages/CreateVoucher.tsx

import React, { useState } from 'react'
import axios from 'axios'
import { Keypair, Networks } from 'soroban-client'
// pull in the Soroban‐RPC tools
import { SorobanRpc } from '@stellar/stellar-sdk'

// pull in the Horizon client under its namespace
import { Horizon } from '@stellar/stellar-sdk'
import { Vault, SorobanNetwork } from 'defindex-sdk'
import { SorobanContextType } from '@soroban-react/core'
interface VoucherForm {
    amount: string
    asset_id: string
    recipient_address: string
    expiration_date: string
}
interface Chain {
    id: SorobanNetwork
    rpcUrl: string
    networkPassphrase: string
}

const DEMO_CHAIN: Chain = {
  id:                 SorobanNetwork.TESTNET,
  rpcUrl:             import.meta.env.VITE_SOROBAN_RPC_URL as string,
  networkPassphrase:  Networks.TESTNET
}
export default function CreateVoucherPage() {

    const [form, setForm] = useState<VoucherForm>({
        amount: '',
        asset_id: 'USDC',
        recipient_address: '',
        expiration_date: ''
    })
    const [status, setStatus] = useState<'idle' | 'loading' | 'done' | 'error'>('idle')
    const [message, setMessage] = useState<string>('')

    const RPC_URL = import.meta.env.VITE_SOROBAN_RPC_URL as string
    const VAULT_ID = import.meta.env.VITE_VAULT_CONTRACT as string
    const PRIVATE_KEY = import.meta.env.VITE_PRIVATE_KEY as string
    const API_BASE = import.meta.env.VITE_API_BASE_URL as string

    // 1) Initialize the DeFindex Vault instance
    const vault = new Vault({
        network: SorobanNetwork.TESTNET,
        contractId: VAULT_ID
    })
    // inline your Testnet chain config
    const testnetChain = {
        id: SorobanNetwork.TESTNET,
        rpcUrl: RPC_URL,
        networkPassphrase: Networks.TESTNET,
        network: SorobanNetwork.TESTNET,
        networkUrl: RPC_URL
    }
    // 1b) Create a “full” SorobanContextType
    const sorobanContext: SorobanContextType = {
        // these wallet fields are unused because we sign manually,
        // but the SDK type requires them
        server: new SorobanRpc.Server(RPC_URL),
        chains: [DEMO_CHAIN],
        connectors: [],
        connect: async () => { /* no-op */ },
        disconnect: async () => { /* no-op */ },
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setForm(prev => ({ ...prev, [e.target.name]: e.target.value }))
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        setStatus('loading')
        setMessage('')

        try {
            // derive issuer address
            const keypair = Keypair.fromSecret(PRIVATE_KEY)
            const issuer = keypair.publicKey()
            console.log("Using RPC_URL:", RPC_URL)
            console.log("Soroban RPC connected:", !!sorobanContext.server)

            // 2) deposit into the vault
            const txHash = await vault.deposit(
                issuer,                   // who's depositing
                Number(form.amount),      // amount
                true,                     // pay fee from source account
                sorobanContext,           // context with server, passphrase, wallet stubs
                PRIVATE_KEY               // signer
            )

            // 3) persist voucher metadata
            await axios.post(`${API_BASE}/api/vouchers`, {
                voucher_code: txHash,
                amount: form.amount,
                asset_id: form.asset_id,
                issuer,
                recipient_address: form.recipient_address,
                expiration_date: form.expiration_date,
                vault_contract: VAULT_ID,
                deposit_tx_hash: txHash
            })

            setMessage(`✅ Voucher issued! TxHash: ${txHash}`)
            setStatus('done')

        } catch (err: any) {
            console.error(err)
            setMessage(`❌ Issue failed: ${err.message}`)
            setStatus('error')
        }
    }

    return (
        <form onSubmit={handleSubmit} style={{ maxWidth: 500, margin: '2rem auto' }}>
            <h2>Create Voucher & Deposit to Vault</h2>

            <label>
                Amount
                <input
                    name="amount"
                    type="number"
                    value={form.amount}
                    onChange={handleChange}
                    placeholder="100"
                    required
                />
            </label>

            <label>
                Recipient Address
                <input
                    name="recipient_address"
                    type="text"
                    value={form.recipient_address}
                    onChange={handleChange}
                    placeholder="G... stellar address"
                    required
                />
            </label>

            <label>
                Expiration Date
                <input
                    name="expiration_date"
                    type="datetime-local"
                    value={form.expiration_date}
                    onChange={handleChange}
                />
            </label>

            <button type="submit" disabled={status === 'loading'}>
                {status === 'loading' ? 'Issuing…' : 'Issue & Deposit'}
            </button>

            {message && (
                <p style={{ marginTop: '1rem', color: status === 'error' ? 'red' : 'green' }}>
                    {message}
                </p>
            )}
        </form>
    )
}
